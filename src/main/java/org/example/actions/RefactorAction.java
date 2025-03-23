package org.example.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.example.ollama.OllamaClient;

import javax.swing.*;
import java.awt.*;

public class RefactorAction extends AnAction {
    private JDialog loadingDialog;
    private JProgressBar progressBar;
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
//        Project project = event.getProject();
//        Editor editor = event.getData(CommonDataKeys.EDITOR);
//        PsiFile file = event.getData(CommonDataKeys.PSI_FILE);
//
//        if (project == null || editor == null || file == null) {
//            return;
//        }
//
//        PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
//        if (element == null) {
//            return;
//        }
        Project project = event.getProject();
// Step 1: Get the selected content
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            Notifications.Bus.notify(new Notification(
                    "CodeRefactoring", "Error", "No active editor found.", NotificationType.ERROR));
            return;
        }

        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            Notifications.Bus.notify(new Notification(
                    "CodeRefactoring", "No Selection", "Please select code to refactor.", NotificationType.WARNING));
            return;
        }
        String refactoredText = selectedText.replace("\n", " ");

        // Show loading dialog with a spinner
        showLoadingDialog(refactoredText);


//        String code = element.getText();

//
//        // Step 3: Show Popup with the Refactored Content
//        JBPopup popup = JBPopupFactory.getInstance()
//                .createMessage("Refactored Code:\n" + refactoredText);
//        popup.showInBestPositionFor(editor);x1
        // Show a popup with selected text (Replace this with your actual refactoring logic)
//        Messages.showMessageDialog(project, "Refactoring: " + response, "Code Refactoring", Messages.getInformationIcon());
//
    }


    // Function to show the loading dialog with a spinner
    private void showLoadingDialog(String text) {
        SwingUtilities.invokeLater(() -> {

        });
        loadingDialog = new JDialog();
        loadingDialog.setTitle("Refactoring in Progress...");
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setSize(300, 100);
        loadingDialog.setLocationRelativeTo(null);  // Center the dialog

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);  // Show an indeterminate progress bar (spinner)
        panel.add(progressBar, BorderLayout.CENTER);
        loadingDialog.add(panel);
        // Allow manual closing by setting a listener
        loadingDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Optionally handle manual closure
                hideLoadingDialog();
            }

            public void windowOpened(java.awt.event.WindowEvent windowEvent) {
                String response = OllamaClient.analyzeCode(text);  // AI refactoring response
                hideLoadingDialog();
                showRefactoringResult(response);
            }
        });
        loadingDialog.setModal(true);  // Make the dialog modal so it blocks the interaction
        loadingDialog.setVisible(true);
    }

    // Function to hide the loading dialog
    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isVisible()) {
            loadingDialog.setVisible(false);
            loadingDialog.dispose();
        }
    }

    // Function to display the result of the refactoring
    private void showRefactoringResult(String selectedText) {
        Messages.showMessageDialog("Refactoring complete for:\n " + selectedText, "Code Refactoring", Messages.getInformationIcon());
    }


}
