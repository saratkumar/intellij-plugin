package org.example.inspections;

import com.intellij.codeInspection.*;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.example.ollama.OllamaClient;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class LongMethodInspection extends LocalInspectionTool {

    private static final int MAX_METHOD_LENGTH = 20;  // Max lines for a method

    LongMethodInspection() {
        System.out.println("File gets loaded into the context");
    }

    @Override
    public ProblemDescriptor @NotNull [] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        List<ProblemDescriptor> problems = new ArrayList<>();

        // Get all classes in the file
        PsiClass[] classes = PsiTreeUtil.getChildrenOfType(file, PsiClass.class);
        if (classes == null) {
            return ProblemDescriptor.EMPTY_ARRAY;
        }

        for (PsiClass psiClass : classes) {
            // Get all methods in the class
            PsiMethod[] methods = psiClass.getMethods();
            for (PsiMethod method : methods) {
                PsiCodeBlock body = method.getBody(); // Get the method body
                if (body != null) {
                    int lines = countCodeLines(body);
                    System.out.println("Method: " + method.getName() + ", Lines: " + lines);  // Debugging log

                    if (lines > MAX_METHOD_LENGTH) {
                        System.out.println("Long method detected: " + method.getName());  // Debugging log
                        // If the method is too long, report a problem
                        ProblemDescriptor descriptor = manager.createProblemDescriptor(
                                method.getNameIdentifier(), // Start element (could also be the method itself)
                                "This method is too long and can be refactored",  // Problem description
                                true, // Whether the problem is fixable
                                ProblemHighlightType.GENERIC_ERROR_OR_WARNING, // Highlight type
                                isOnTheFly // Whether it is in real-time or batch inspection
                        );
                        problems.add(descriptor);
                    }
                }
            }
        }

        return problems.toArray(new ProblemDescriptor[0]);
    }

    private int countCodeLines(PsiCodeBlock body) {
        // Count non-empty lines of code in the body
        String[] lines = body.getText().split("\n");
        int count = 0;
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public @NotNull String getDisplayName() {
        return "Long Method Inspection";
    }

    @Override
    public @NotNull String getGroupDisplayName() {
        return "General Inspection"; // This specifies the group in which the inspection will appear
    }

//    @Override
//    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
//        return new JavaElementVisitor() {
//            @Override
//            public void visitMethod(PsiMethod method) {
//                String code = method.getText().replace("\n", " ");
//                holder.registerProblem(method, "AI is analyzing the code...", ProblemHighlightType.INFORMATION);
//                // Run the request asynchronously (on a background thread)
//                new Thread(() -> {
//                    // Call OllamaClient to analyze the code
//                    String response = OllamaClient.analyzeCode(code);
//                    System.out.println(response +"____Resopone");
//                    // Once the response is received, update the UI on the EDT thread
//                    SwingUtilities.invokeLater(() -> {
//                        if (response.contains("error")) {
//                            // If there's an error, show it in the problem holder
//                            holder.registerProblem(method, "AI Suggestion Error: " + response, ProblemHighlightType.WARNING);
//                        } else {
//                            // Show the suggestion in the problem holder
//                            holder.registerProblem(method, "AI Suggestion: " + response, ProblemHighlightType.WEAK_WARNING);
//                        }
//                    });
//                }).start();  // Start the async thread
//            }
//        };
//    }

}
