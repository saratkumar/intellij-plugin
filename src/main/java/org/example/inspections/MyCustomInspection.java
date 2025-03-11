package org.example.inspections;

import com.intellij.codeInspection.*;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.example.ollama.OllamaClient;

public class MyCustomInspection extends AbstractBaseJavaLocalInspectionTool {
    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethod(PsiMethod method) {
                String code = method.getText();
                String response = OllamaClient.analyzeCode(code);

                if (!response.startsWith("Error")) {
                    holder.registerProblem(method, "AI Suggestion: " + response, ProblemHighlightType.WEAK_WARNING);
                }
            }
        };
    }

    @Override
    public @Nls String getDisplayName() {
        return "AI-Powered Java Code Analysis";
    }

    @Override
    public @Nls String getGroupDisplayName() {
        return "JavaSK Code Analysis";
    }
}
