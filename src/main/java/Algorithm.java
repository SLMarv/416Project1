
import java.util.*;

public class Algorithm {
        static class Node {
            String name;

            Node(String name) {
                this.name = name;
            }
        }

        static class Edge {
            Node source, destination;

            Edge(Node source, Node destination) {
                this.source = source;
                this.destination = destination;
            }
        }

        static class Switch extends Node {
            List<Node> connectedNodes;

            Switch(String name) {
                super(name);
                this.connectedNodes = new ArrayList<>();
            }
        }

        static class Computer extends Node {
            Switch connectedSwitch;

            Computer(String name, Switch connectedSwitch) {
                super(name);
                this.connectedSwitch = connectedSwitch;
            }
        }

        static class VirtualNetworkBuilder {
            List<Node> nodes;
            List<Edge> edges;

            VirtualNetworkBuilder() {
                nodes = new ArrayList<>();
                edges = new ArrayList<>();
            }

            void addSwitch(String name) {
                Switch switchNode = new Switch(name);
                nodes.add(switchNode);
            }

            void addComputer(String name, Switch connectedSwitch) {
                Computer computerNode = new Computer(name, connectedSwitch);
                nodes.add(computerNode);
                connectedSwitch.connectedNodes.add(computerNode);
            }

            void connectNodes(Node source, Node destination) {
                edges.add(new Edge(source, destination));
            }

            void buildSpanningTree() {

                    List<Edge> sortedEdges = new ArrayList<>(edges);
                    Collections.sort(sortedEdges, Comparator.comparingInt(e -> ((Switch) e.source).connectedNodes.size()));

                    Map<Node, Integer> components = new HashMap<>();
                    List<Edge> spanningTreeEdges = new ArrayList<>();

                    for (Node node : nodes) {
                        components.put(node, components.size());
                    }

                    for (Edge edge : sortedEdges) {
                        int component1 = components.get(edge.source);
                        int component2 = components.get(edge.destination);

                        if (component1 != component2) {
                            spanningTreeEdges.add(edge);

                            // Merge components
                            for (Node node : nodes) {
                                if (components.get(node) == component2) {
                                    components.put(node, component1);
                                }
                            }
                        }
                    }

                    System.out.println("Spanning Tree Edges: " + spanningTreeEdges);
                }
            }

        public static void main(String[] args) {
            VirtualNetworkBuilder builder = new VirtualNetworkBuilder();

            // Adding switches
            builder.addSwitch("SwitchA");
            builder.addSwitch("SwitchB");

            // Adding computers
            Switch switchA = (Switch) builder.nodes.get(0);
            builder.addComputer("Computer1", switchA);
            builder.addComputer("Computer2", switchA);

            Switch switchB = (Switch) builder.nodes.get(1);
            builder.addComputer("Computer3", switchB);
            builder.addComputer("Computer4", switchB);

            // Connecting nodes
            builder.connectNodes(switchA, switchB);
            builder.connectNodes(switchA, switchA.connectedNodes.get(0));
            builder.connectNodes(switchB, switchB.connectedNodes.get(1));


            builder.buildSpanningTree();
            System.out.println("Virtual Network: " + builder.nodes);
        }
    }


