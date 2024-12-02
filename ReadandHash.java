import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ReadandHash {
    

    public static Map<Integer, String> fileArgs() {
        Map<Integer, String> option = new HashMap<>();
        option.put(1, "TheRavenB24.txt");
        option.put(2, "TellTaleHeartB24.txt");
        option.put(3, "TheCaskOfAmontilladoB24.txt");

        String fileFor3 = option.get(3);
        if (fileFor3 != null) {
            option.put(4, "2" + fileFor3);
        }

        String fileFor2 = option.get(2);
        if (fileFor2 != null) {
            option.put (4, "3" + fileFor3);
        }

        return option;
    }

    public static Map<Integer, Integer> tableSize() {
        Map<Integer, Integer> mparam = new HashMap<>();
        mparam.put(1, 1000);
        mparam.put(2, 790);
        mparam.put(3, 991);
        mparam.put(4, 1499);
        mparam.put(5, 1499);
        return mparam;
    }

    public static void ProcessFile(String filePath, int C, int tableSize, String[] hashTable, int fileArg) {
        Pattern wordPattern = Pattern.compile("[A-Za-z'-]+");
        BufferedReader br = null;
        try {
            if (fileArg == 4) {

                String path2 = fileArgs().get(2);
                if (path2 != null) {
                    br = new BufferedReader(new FileReader(path2));
                    processBufferedReader(br, wordPattern, hashTable, C, tableSize);
                } else {
                    System.out.println("Error: File path for 2 is missing.");
                }

                String path3 = fileArgs().get(3);
                if (path3 != null) {
                    br = new BufferedReader(new FileReader(path3));
                    processBufferedReader(br, wordPattern, hashTable, C, tableSize);
                } else {
                    System.out.println("Error: File path for 3 is missing.");
                }
            } else if(fileArg == 5) {
                String path3 = fileArgs().get(3);
                if (path3 != null) {
                    br = new BufferedReader(new FileReader(path3));
                    processBufferedReader(br, wordPattern, hashTable, C, tableSize);
                } else {
                    System.out.println("Error: File path for 3 is missing.");
                }

                String path2 = fileArgs().get(3);
                if (path2 != null) {
                    br = new BufferedReader(new FileReader(path2));
                    processBufferedReader(br, wordPattern, hashTable, C, tableSize);
                } else {
                    System.out.println("Error: File path for 2 is missing.");
                }

            } else {
                br = new BufferedReader(new FileReader(filePath));
                processBufferedReader(br, wordPattern, hashTable, C, tableSize);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void processBufferedReader(BufferedReader br, Pattern wordPattern, String[] hashTable, int C, int tableSize) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            Matcher matcher = wordPattern.matcher(line);
            while (matcher.find()) {
                String word = matcher.group();
                insertIntoHashTable(word, hashTable, C, tableSize);
            }
        }
    }

    public static int HashWords(String word, int C, int m) {
        int h = 0;
        for (int i = 0; i < word.length(); i++) {
            h = (h * C + word.charAt(i)) % m;
        }
        return h;
    }

    public static void insertIntoHashTable(String word, String[] hashTable, int C, int tableSize) {
        int hashValue = HashWords(word, C, tableSize);
        int hashAddress = hashValue;

        int attempts = 0;
        while (hashTable[hashAddress] != null) {
            if (hashTable[hashAddress].equals(word)) {
                return;
            }
            hashAddress = (hashAddress + 1) % tableSize;
            attempts++;
            if (attempts >= tableSize) {
                System.out.println("Error: Hash table is full, unable to insert word: " + word);
                return;
            }
        }

        hashTable[hashAddress] = word;
        System.out.println("Hash Address: " + hashAddress + ", Hashed Word: " + word + ", Hash Value: " + hashValue);
    }

    public static void displayHashTable(String[] hashTable) {
        for (int i = 0; i < hashTable.length; i++) {
            System.out.println("Index " + i + ": " + (hashTable[i] == null ? "-1" : hashTable[i]));
        }
    }

    public static void findLoadFactor(String[] hashTable) {
        int notEmpty = 0;
        for (String entry : hashTable) {
            if (entry != null && !entry.equals("-1")) {
                notEmpty++;
            }
        }
        double alpha = (double) notEmpty / hashTable.length;
        System.out.println("Non-Empty Addresses: " + notEmpty);
        System.out.println("Load Factor (α) : " + alpha);
    }

    public static void findLongestEmptyArea(String[] hashTable) {
        int longestEmpty = 0;
        int currentEmpty = 0;
        int startIdxGlb = -1;
        int startIdxCur = -1;

        for (int i = 0; i < hashTable.length * 2; i++) {
            int idx = i  % hashTable.length;


        if (hashTable[idx] == null || hashTable[idx].equals("-1")) {
            if (currentEmpty == 0) {
                startIdxCur = idx;
            }
            currentEmpty ++;
        }

        else {
            if (currentEmpty > longestEmpty)  {
                longestEmpty = currentEmpty;
                startIdxGlb = startIdxCur;
            }
            currentEmpty = 0;
            }
        }
        if (currentEmpty > longestEmpty) {
            longestEmpty = currentEmpty;
            startIdxGlb = startIdxCur;
        }
        System.out.println("Longest Empty Area:" + longestEmpty + "starting at index " + startIdxGlb);
    }

    
    public static void findLargestCluster(String[] hashTable) {
        int longestCluster = 0;
        int currentCluster = 0;
        int longestIdxGlb = -1;
        int longestIdxCur = -1;

        for (int i = 0; i < hashTable.length * 2; i++) {
            int idx =  i % hashTable.length;

        if (hashTable[idx] != null && !hashTable[idx].equals("-1")) {
            if (currentCluster == 0){
                longestIdxCur = idx;
            }
            currentCluster++;
        }
        else {
            if (currentCluster > longestCluster) {
                longestCluster = currentCluster;
                longestIdxGlb = longestIdxCur;
            }
            currentCluster = 0;
        }
    }

    if (currentCluster > longestCluster) {
        longestCluster = currentCluster;
        longestIdxGlb = longestIdxCur;
    }

    System.out.println("The longest cluster is: " + longestCluster + "starting at  " + longestIdxGlb);

}

    public static void findMostCommonHashVal(String[] hashTable, int C, int m) {
        Map<Integer, Integer> hashCounts = new HashMap<>();

        for (String word : hashTable) {
            if (word != null) {
                int hashVal = HashWords(word, C, m);
                hashCounts.put(hashVal, hashCounts.getOrDefault(hashVal, 0) + 1);
            }
        }

        int mostCommonHash = -1;
        int maxCount = 0;

        for (Map.Entry<Integer, Integer> entry : hashCounts.entrySet()) {
            int val = entry.getValue();

            if (val > maxCount) {
                maxCount = val;
                mostCommonHash = entry.getKey();
            }
        }
        System.out.println("Most common Hash : " + mostCommonHash + " with " + maxCount + " words");
    }

    public static void farthestWord(String[] hashTable, int C, int m) {
        int maxDistance = 0;
        String farthestWord = null;

        for (int i = 0; i < hashTable.length; i++) {
            String word = hashTable[i];
            if (word != null && !word.equals("-1")) {
                int actualVal = HashWords(word, C, m);
                int distance = Math.abs (i - actualVal);

                if (distance > m /2) {
                    distance = m-distance;
                }
                if (distance > maxDistance) {
                    maxDistance = distance;
                    farthestWord = word;
                }
            }
        }
        System.out.println("word farthest from hash: " + farthestWord);
        System.out.println("Distance from hash val: " + maxDistance);
    }

    public static void main(String[] args) {
        int C = 123;

        if (args.length < 1) {
            System.out.println("Please provide the file argument (e.g., 1, 2, 3, 4, or 5.)");
            return;
        }

        int fileArg;
        try {
            fileArg = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid argument. Please provide an integer between 1 and 5.");
            return;
        }

        if (fileArg < 1 || fileArg > 5) {
            System.out.println("Invalid file argument. Choose between 1, 2, 3, 4, or 5.");
            return;
        }

        Map<Integer, Integer> mparams = tableSize();
        int m = mparams.get(fileArg);

        Map<Integer, String> fileArgs = fileArgs();
        String path = fileArgs.get(fileArg); // Get file path

        if (path == null && fileArg != 4 && fileArg != 5) { // Allow `4 and 5` since they use multiple files
            System.out.println("Invalid file argument.");
            return;
        }


//        System.out.println(HashWords("Raven", C, m)); checking hash fn
        String[] hashTable = new String[m];
        ProcessFile(path, C, m, hashTable, fileArg);

        System.out.println("\nFinal Hash Table:");
        displayHashTable(hashTable);

        System.out.println("a. How many non-empty addresses are there in the table? What does that make\n" +
                "the load factor, α, for our table?");
        findLoadFactor(hashTable);

        System.out.println("b. What is the longest empty area in the table, and where is it?");
        findLongestEmptyArea(hashTable);

        System.out.println("c. What is the longest (largest) cluster in the table, and where is it?");
        findLargestCluster(hashTable);

        System.out.println("d. What hash value results from the greatest number of distinct words, and how\n" +
                "many words have that hash value?");
        findMostCommonHashVal(hashTable, C, m);

        System.out.println("e. What word is placed in the table farthest from its actual hash value, and\n" +
                "how far away is it from its actual hash value?\n");
        farthestWord(hashTable, C, m);

    }
}
