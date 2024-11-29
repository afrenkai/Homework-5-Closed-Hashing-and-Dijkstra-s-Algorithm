import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;

public class Djikstra {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the file path as a command-line argument.");
            return;
        }

        String filePath = args[0];
        int[][] graph = readGraphFromFile(filePath);

        if (graph == null) {
            System.out.println("Failed to read the graph from the file.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the source node:");
        String sourceNode = scanner.nextLine().toUpperCase();

        System.out.println("Enter the target node:");
        String targetNode = scanner.nextLine().toUpperCase();

        Map<String, Integer> nodes = createNodeMapping();
        if (!nodes.containsKey(sourceNode) || !nodes.containsKey(targetNode)) {
            System.out.println("Invalid source or target node. Please use valid node names.");
            return;
        }

        int sourceIndex = nodes.get(sourceNode);
        int targetIndex = nodes.get(targetNode);

        dijkstra(graph, sourceIndex, targetIndex, nodes);
    }

    public static int[][] readGraphFromFile(String filePath) {
        List<int[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.trim().split("\\s+"); //one or more whitespace characters
                int[] row = Arrays.stream(values).mapToInt(Integer::parseInt).toArray();
                rows.add(row);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }
        return rows.toArray(new int[rows.size()][]);
    }

//    public static void displayGraph(int[][] graph) {
//        for (int[] row : graph) {
//            for (int value : row) {
//                System.out.print(value + " ");
//            }
//            System.out.println();
//        }
//    }
    // test to make sure my graph is treated as a matrix

    public static Map<String, Integer> createNodeMapping() {
        Map<String, Integer> nodes = new HashMap<>();
        nodes.put("A", 0);
        nodes.put("J", 1);
        nodes.put("M", 2);
        nodes.put("R", 3);
        nodes.put("K", 4);
        nodes.put("S", 5);
        nodes.put("I", 6);
        nodes.put("N", 7);
        nodes.put("T", 8);
        nodes.put("D", 9);
        return nodes;
    }
    // for a cool little thing where I swap the ints and strs so the user puts in an edge by str id and I do the process in
    // ints and then return the strs.

    public static void dijkstra(int[][] graph, int source, int target, Map<String, Integer> nodes) {
        int numNodes = graph.length;
        int[] distances = new int[numNodes];
        boolean[] visited = new boolean[numNodes];
        int[] predecessors = new int[numNodes]; // predecessor arr to track path taken so I can print later

        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(predecessors, -1);
        distances[source] = 0;

        for (int i = 0; i < numNodes; i++) {
            int u = selectMinVertex(distances, visited);
            if (u == -1) break; // means we cant get to other nodes so we need to stop

            visited[u] = true;
            System.out.println("\nStep " + (i + 1) + ":");
            System.out.println("Selected node: " + getNodeByValue(nodes, u) + " with distance: " + distances[u]);

            for (int v = 0; v < numNodes; v++) {
                if (!visited[v] && graph[u][v] != 0 && distances[u] != Integer.MAX_VALUE
                        && distances[u] + graph[u][v] < distances[v]) {
                    distances[v] = distances[u] + graph[u][v];
                    predecessors[v] = u;
                    System.out.println("Updated distance of node " + getNodeByValue(nodes, v) + " to: " + distances[v]);
                }
            }
        }

        printShortestPath(distances, source, target, predecessors, nodes);
    }

    public static int selectMinVertex(int[] distances, boolean[] visited) {
        int min = Integer.MAX_VALUE;
        int minIdx = -1;

        for (int i = 0; i < distances.length; i++) {
            if (!visited[i] && distances[i] < min) {
                min = distances[i];
                minIdx = i;
            }
        }
        return minIdx;
    }

    public static void printShortestPath(int[] distances, int source, int target, int[] predecessors, Map<String, Integer> nodes) {
        System.out.println("\nShortest path from " + getNodeByValue(nodes, source) + " to " + getNodeByValue(nodes, target) + ":");
        if (distances[target] == Integer.MAX_VALUE) {
            System.out.println("No path exists.");
            return;
        }
        System.out.println("Distance: " + distances[target]);

        List<String> path = new ArrayList<>();
        for (int at = target; at != -1; at = predecessors[at]) {
            path.add(getNodeByValue(nodes, at));
        }
        Collections.reverse(path); // lol
        System.out.println("Path: " + String.join(" -> ", path)); // for ease of printing and showing order
    }

    public static String getNodeByValue(Map<String, Integer> nodes, int value) {
        for (Map.Entry<String, Integer> entry : nodes.entrySet()) {
            if (entry.getValue() == value) {
                return entry.getKey();
            }
        }
        return null;
    }
}
