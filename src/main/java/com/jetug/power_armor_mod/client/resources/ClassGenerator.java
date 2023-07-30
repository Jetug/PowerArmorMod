package com.jetug.power_armor_mod.client.resources;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class ClassGenerator {

    public static void generateConstants(String name, Collection<String> fields) {
        var fileName = formatClassName(name);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/jetug/power_armor_mod/generated/" + fileName + ".java"))) {
            writer.write("package com.jetug.power_armor_mod.generated;\n\n");
            writer.write("public class " + fileName + " {\n\n");
            for (String field : fields) {
                writeConstant(writer, field);
            }
            writer.write("}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String formatClassName(String fileName) {
        String className = fileName.substring(0, fileName.indexOf('.'));
        StringBuilder formattedClassName = new StringBuilder();

        String[] words = className.split("_");
        for (String word : words) {
            if (word.length() > 0) {
                formattedClassName.append(
                        Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase()
                );
            }
        }

        return formattedClassName.toString();
    }

    private static void writeConstant(BufferedWriter writer, String field) throws IOException {
        String constantField = "\tpublic static final String " + field.toUpperCase() + " = \"" + field + "\";\n\n";
        writer.write(constantField);
    }
}