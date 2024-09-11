package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PaymentLink {

    @FXML
    private TextField IDNumText;

    @FXML
    private TextField creditCardTxt;

    @FXML
    private TextField emailText;

    @FXML
    private TextField fullNameText;

    @FXML
    private Button payBtn;

    @FXML
    private TextField phoneText;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Button backBtn; // Added for the Back button

    @FXML
    void payForProduct(ActionEvent event) {
        List<String> errorMessages = new ArrayList<>();

        // Reset field styles before validation
        resetFieldStyles();

        // Validate each field and track errors
        if (fullNameText.getText().isEmpty() || !validateFullName()) {
            highlightFieldError(fullNameText);
            errorMessages.add("Invalid full name. Please use only letters and spaces.");
        }

        if (IDNumText.getText().isEmpty() || !validateIDNumber()) {
            highlightFieldError(IDNumText);
            errorMessages.add("Invalid ID number. Please enter a 9-digit number.");
        }

        if (phoneText.getText().isEmpty() || !validatePhoneNumber()) {
            highlightFieldError(phoneText);
            errorMessages.add("Invalid phone number. Please enter a 10-digit number.");
        }

        if (emailText.getText().isEmpty() || !validateEmail()) {
            highlightFieldError(emailText);
            errorMessages.add("Invalid email format.");
        }

        if (creditCardTxt.getText().isEmpty() || !validateCreditCard()) {
            highlightFieldError(creditCardTxt);
            errorMessages.add("Invalid credit card number. Please enter a 16-digit number.");
        }

        // Display error message based on the number of errors
        if (!errorMessages.isEmpty()) {
            String alertMessage;
            if (errorMessages.size() == 1) {
                alertMessage = errorMessages.get(0);
            } else {
                alertMessage = "Multiple errors detected. Please review the highlighted fields and correct the issues.";
            }

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation Errors");
                alert.setHeaderText(null);
                alert.setContentText(alertMessage);
                alert.show();
            });
            return; // Stop processing if validation fails
        }

        // If everything is valid, proceed with the payment
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Payment completed successfully!");
            alert.show();
        });
        MovieDetailsPage.movieDetailsPage = 0;
    }

    private void resetFieldStyles() {
        fullNameText.getStyleClass().remove("error");
        IDNumText.getStyleClass().remove("error");
        phoneText.getStyleClass().remove("error");
        emailText.getStyleClass().remove("error");
        creditCardTxt.getStyleClass().remove("error");
    }

    private boolean validateFullName() {
        String fullName = fullNameText.getText().trim();
        return Pattern.matches("[a-zA-Z\\s]+", fullName);
    }

    private boolean validateIDNumber() {
        String idNumber = IDNumText.getText().trim();
        return Pattern.matches("\\d{9}", idNumber);
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = phoneText.getText().trim();
        return Pattern.matches("\\d{10}", phoneNumber);
    }

    private boolean validateEmail() {
        String email = emailText.getText().trim();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean validateCreditCard() {
        String creditCard = creditCardTxt.getText().trim();
        return Pattern.matches("\\d{16}", creditCard);
    }

    private void highlightFieldError(TextField field) {
        field.getStyleClass().add("error");
    }

    @FXML
    private void switchToPreviousPage(ActionEvent event) throws IOException {
        App.switchScreen("PurchaseLink"); // in case he wants to come back
    }

    @FXML
    private void switchToCardsPage() throws IOException {
        MovieDetailsPage.movieDetailsPage = 0;
        App.switchScreen("CardsPage");
    }

    @FXML
    private void switchToHostPage() throws IOException {
        MovieDetailsPage.movieDetailsPage = 0;
        App.switchScreen("HostPage");
    }

    @FXML
    private void switchToHomePage() throws IOException {
        MovieDetailsPage.movieDetailsPage = 0;
        App.switchScreen("HomePage");
    }

    @FXML
    private void switchToComplaintPage() throws IOException {
        MovieDetailsPage.movieDetailsPage = 0;
        App.switchScreen("ComplaintPage");
    }

    @FXML
    private void switchToLoginPage() throws IOException {
        MovieDetailsPage.movieDetailsPage = 0;
        App.switchScreen("LoginPage");
    }

    @FXML
    private void switchToChargebackPage() throws IOException {
        MovieDetailsPage.movieDetailsPage = 0;
        App.switchScreen("ChargebackPage");
    }

    @FXML
    public void switchToMoviesPage() throws IOException {
        MovieDetailsPage.movieDetailsPage = 0;
        App.switchScreen("MoviesPage");
    }
}
