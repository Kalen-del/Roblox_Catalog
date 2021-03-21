import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

// Using LCS algorithm to fuzzy search.
// https://en.wikipedia.org/wiki/Longest_common_subsequence_problem
// The search rating system is based on a old system I've made before for the Roblox Developer Community:
// https://devforum.roblox.com/t/simple-fuzzy-search/534780
// I came up with the rating system myself (not algorithm), but if there is a source on the internet with a similar system
// then I take no credit in the creation of this system. There probably is since this formula is not that difficult.

/**
 * Object which searches through items based on query.
 */
public class FuzzySearch {
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- CONSTANTS
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private double          MIN_SEARCH_ACCURACY = 1, ITEM_NAME_BONUS = 0.5;
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- OBJECT VALUES
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private double          searchAccuracy;
    private double          baseSearchValue;
    private ArrayList<Item> pageItems; // Store passed objects through here.
    private boolean         shouldIncludeWhiteSpace;

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- PUBLIC METHODS
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Adds new item object to the fuzzy search object.
     *
     * @param newItem the new item.
     */
    public void addItem(Item newItem){
        pageItems.add(newItem);
    }

    /**
     * Gets an array list with best results in order based on query.
     * @param query the string that should be queried.
     * @return An array list of item objects.
     */
    public ArrayList<Item> getSearchResults(String query){
        int queryLength = query.length();
        // Account for changing accuracy as character increases;
        HashMap<Item, Double> ratingMap = new HashMap<>(); // Put the results in a hashmap for easier sorting of ratings later.
        ArrayList<Item> searchResults   = new ArrayList<>(); // Store the best results in here.
        if(!shouldIncludeWhiteSpace){ // If we have to strip the whitespace
            query = query.replaceAll("\\s+", "").toLowerCase(); //Use the regex pattern to replace all the whitespace.
        }
        for(Item item: pageItems){
            double bestRating = -10000; // Keep the rating as a very low number for comparisons.
            double rating = getSearchRating(query, item.toString(), false); // Get the rating for the name of the item.
            bestRating = Math.max(bestRating, rating); // Update the best rating.
            String[] itemTags = item.getItemTags(); // Get the item's tags.
            for (String itemTag : itemTags) { // Iterate through the tags.
                rating     = getSearchRating(query, itemTag, true); // Get the score for the tag.
                bestRating = Math.max(bestRating, rating); // Check if this is the highest rating.
            }
            if (bestRating >= baseSearchValue) { // If the best rating for the item is over the base search value
                ratingMap.put(item, bestRating); // Put it into the best objects map.
            }
        }
        // Sort the map
        ratingMap.entrySet().stream() // Make it into a sequence of objects.
                .sorted((key1, key2) -> -key1.getValue().compareTo(key2.getValue())) // Compared the two values from the key.
                .forEach(key -> searchResults.add(key.getKey())); // Copy each of the keys over now.


        return searchResults;
    }

    /**
     * Calculates the fuzzy search rating based on the query string and a comparison string.
     * @param query the string of the query.
     * @param comparedString the string that the query will be compared to.
     * @param isTag if the compared to string is a tag or not.
     * @return a double which represents the query's rating.
     */
    private double getSearchRating(String query, String comparedString, boolean isTag){
        int[][] dp = new int[query.length()+1][comparedString.length()+1]; // Set the the dp table.
        double score;
        if (!shouldIncludeWhiteSpace){
            comparedString = comparedString.replaceAll("\\s+", ""); // Strip the whitespace using regex pattern.
        }
        comparedString = comparedString.toLowerCase(); // Keep all the strings lowercase for accurate matching.
        for(int i = 0; i <= query.length(); i++){
            for(int j = 0; j <= comparedString.length(); j++){
                if(i == 0 || j == 0){ // if the index is 0 we should set this cell as 0.
                    dp[i][j] = 0;
                }else if(query.charAt(i-1) == comparedString.charAt(j-1)){ // If the current character is equal to the query's character.
                    dp[i][j] = dp[i-1][j-1] + 1; // Get the previous cell's solution and increase it by one since there is a match.
                }else{ // If query's character does not match with the comparison's string's character.
                    dp[i][j] = Math.max(dp[i][j-1], dp[i-1][j]); // Get the best solution from the previous cell and this solution.
                }
            }
        }
        score = dp[query.length()][comparedString.length()]; // Get the longest common subsequence.
        if(score > ((double)query.length()/searchAccuracy)){ // Compare the score using this formula.
            if(!isTag){ // If we are comparing an object's name.
                score += ITEM_NAME_BONUS; // We should increase it's score since the user wants the object's name rather than a tag.
            }
        }else{  // The score is not good enough.
            score = -1000; // Just set the score to a very low number.
        }
        return score;
    }

    public FuzzySearch(boolean shouldIncludeWhiteSpace, double accuracy, ArrayList<Item> fullItemList){
        if (accuracy < 1){ // Accuracy can not be lower than 1 or else the algorithm breaks.
            baseSearchValue = MIN_SEARCH_ACCURACY;
            searchAccuracy  = MIN_SEARCH_ACCURACY;
        }else{
            searchAccuracy  = accuracy;
            baseSearchValue = accuracy;
        }
        this.pageItems               = new ArrayList<Item>(fullItemList); // Shallow copy the passed item list.
        this.shouldIncludeWhiteSpace = shouldIncludeWhiteSpace;
    }
}
