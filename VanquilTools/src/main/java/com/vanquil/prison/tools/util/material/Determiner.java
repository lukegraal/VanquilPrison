//package com.vanquil.prison.tools.util.material;
//
//import com.google.common.collect.Comparators;
//
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.TreeMap;
//
//public class Test {
//    public static void main(String[] args) {
//        String t = null;
//        String[] layers = t.split("\n");
//
//        class Data {
//            final int id;
//            final int data;
//
//            Data(int id, int data) {
//                this.id = id;
//                this.data = data;
//            }
//        }
//
//        TreeMap<Integer, TreeMap<Integer, String>> dataStringMap = new TreeMap<>(Integer::compareTo);
//        for (int i = 0; i < layers.length; i+=2) {
//            try {
//                String name = layers[i].split("\t")[0].replace(" ", "_").toUpperCase();
//                String s = layers[i + 1];
//                s = s.replace("(minecraft:", "")
//                        .replace(")", "");
//                String[] split = s.split("\t");
//                int id = Integer.parseInt(split[1]);
//                int data = Integer.parseInt(split[2]);
//                TreeMap<Integer, String> map = dataStringMap.get(id);
//                if (map != null) {
//                    map.put(data, name);
//                } else {
//                    TreeMap<Integer, String> value = new TreeMap<>();
//                    value.put(data, name);
//                    dataStringMap.put(id, value);
//                }
//            } catch (Throwable tx) {
//                // ignored
//            }
//        }
//
//        for (Map.Entry<Integer, TreeMap<Integer, String>> entry :dataStringMap.entrySet()){
//            int id = entry.getKey();
//
//            for (Map.Entry<Integer, String> integerStringEntry : entry.getValue().entrySet()) {
//                int data = integerStringEntry.getKey();
//                String name = integerStringEntry.getValue();
//                System.out.println(name.toUpperCase() + "(" + id + ", (byte) " + data + "),");
//            }
//        }
//    }
//}
