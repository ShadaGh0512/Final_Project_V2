package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Cinema;
import il.cshaifasweng.OCSFMediatorExample.entities.HomeMoviePurchase;
import il.cshaifasweng.OCSFMediatorExample.entities.NewMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.DecimalFormat;

public class PurchaseLink {

    @FXML
    private TableView<PurchaseItem> purchaseTable = new TableView<>();

    @FXML
    private TableColumn<PurchaseItem, Integer> productNoColumn = new TableColumn<>("Product No");

    @FXML
    private TableColumn<PurchaseItem, String> typeColumn = new TableColumn<>("Type");

    @FXML
    private TableColumn <PurchaseItem, String> movieColumn= new TableColumn<>("Movie Title");

    @FXML
    private TableColumn<PurchaseItem, String> detailsColumn = new TableColumn<>("Details");

    @FXML
    private TableColumn<PurchaseItem, Double> priceColumn = new TableColumn<>("Price");

    @FXML
    private Label totalPriceLabel;

    private ObservableList<PurchaseItem> purchaseItems = FXCollections.observableArrayList();

    private Cinema cinema = new Cinema();
    private double linkTicketValue;

    @FXML
    public void initialize() {
        // Register to EventBus to listen for events
       EventBus.getDefault().register(this);
        requestCinemaFromServer();

        // Set up table columns to link with PurchaseItem fields
        productNoColumn.setCellValueFactory(new PropertyValueFactory<>("productNo"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        movieColumn.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


    }

    @FXML
    private void requestCinemaFromServer() {
        try {
            NewMessage message = new NewMessage("cinema");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Subscribe
    public void onCinemaEvent(UpdateCinemaEvent event) {
        Platform.runLater(() -> {
            Cinema cinema = event.getCinema();
            this.cinema = cinema;
            if( purchaseItems.isEmpty()) {
                purchaseItems.clear();
                this.linkTicketValue = cinema.getLinkTicketPrice();
                PurchaseItem item = new PurchaseItem(MovieLinkDetailsPage.homeMoviePurchase.getId(), "Link Ticket",
                        MovieLinkDetailsPage.homeMoviePurchase.getHomeMovie().getEngtitle(),
                        MovieLinkDetailsPage.homeMoviePurchase.getScreening().myToString(), linkTicketValue);
                // Bind the table view to the observable list
                purchaseItems.add(item);
                purchaseTable.setItems(purchaseItems);

                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String strValue = decimalFormat.format(linkTicketValue);
                totalPriceLabel.setText(strValue + "$");
            }
        });
    }
/*
    @Subscribe
    public void handleUpdateHomeMoviePurchaseEvent(NewMessage message) {
        if (message.getMessage().equals("selectedHomeMoviePurchase")) {
            HomeMoviePurchase selectedHomeMoviePurchase = (HomeMoviePurchase) message.getObject();

            // Add the selected home movie details to the table
            PurchaseItem item = new PurchaseItem(
                    String.valueOf(selectedHomeMoviePurchase.getHomeMovie().getId()),
                    "Home Movie",
                    selectedHomeMoviePurchase.getHomeMovie().getEngtitle(),
                    String.valueOf(selectedHomeMoviePurchase.getPricePaid()) + "$"
            );

            // Add the item to the table view's observable list
            purchaseItems.add(item);

            // Update the total price label
            totalPriceLabel.setText(selectedHomeMoviePurchase.getPricePaid() + "$");
        }
    }*/


    // Inner class to represent purchase items in the table
    public static class PurchaseItem {
        private final int productNo;
        private final String type;
        private final String movieTitle;
        private final String details;
        private final double price;

        public PurchaseItem(int productNo, String type,String movieTitle, String details, double price) {
            this.productNo = productNo;
            this.type = type;
            this.movieTitle = movieTitle;
            this.details = details;
            this.price = price;
        }

        public int getProductNo() {
            return productNo;
        }

        public String getType() {
            return type;
        }

        public String getDetails() {
            return details;
        }

        public double getPrice() {
            return price;
        }

        public String getMovieTitle() {
            return movieTitle;
        }
    }


    @FXML
    public void switchToMoviesPage() throws IOException {
        MovieDetailsPage.movieDetailsPage = 0;
        App.switchScreen("MoviesPage");
    }

    @FXML
    private void switchToPaymentPage() throws IOException {
        App.switchScreen("PaymentLink");
    }


    @FXML
    private void switchToCardsPage() throws IOException {
        MovieDetailsPage.movieDetailsPage = 0;
        App.switchScreen("CardsPage");
    }

    @FXML
    private void switchToMovieDetailsPage() throws IOException {
        Platform.runLater(() -> {
            purchaseItems.clear();
            App.switchScreen("MovieLinkDetailsPage");
        });
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
    private void  switchToPersonalAreaPage() throws IOException {
        MovieDetailsPage.movieDetailsPage = 0;
        App.switchScreen("PersonalAreaPage");
    }

    @Subscribe
    public void onRequestEvent(UpdateRequestEvent event) {}
}

