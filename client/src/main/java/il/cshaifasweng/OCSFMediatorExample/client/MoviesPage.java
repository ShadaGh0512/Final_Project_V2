package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MoviesPage {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label soon;
    @FXML
    private Label NowInCinema;
    @FXML
    private Label WatchFromHome;

    @FXML
    private ImageView cinemaImageView1;
    @FXML
    private ImageView cinemaImageView2;
    @FXML
    private ImageView cinemaImageView3;
    @FXML
    private ImageView cinemaImageView4;
    @FXML
    private Label cinemaLabel1;
    @FXML
    private Label cinemaLabel2;
    @FXML
    private Label cinemaLabel3;
    @FXML
    private Label cinemaLabel4;
    @FXML
    private ImageView soonImageView1;
    @FXML
    private ImageView soonImageView2;
    @FXML
    private ImageView soonImageView3;

    @FXML
    private Label soonLabel1;
    @FXML
    private Label soonLabel2;
    @FXML
    private Label soonLabel3;

    @FXML
    private ImageView homeImageView1;
    @FXML
    private ImageView homeImageView2;
    @FXML
    private ImageView homeImageView3;
    @FXML
    private ImageView homeImageView4;

    @FXML
    private Label homeLabel1;
    @FXML
    private Label homeLabel2;
    @FXML
    private Label homeLabel3;
    @FXML
    private Label homeLabel4;

    @FXML
    private Button leftButton;
    @FXML
    private Button rightButton;

    @FXML
    private Button HomePageB;

    @FXML
    private Button homeLeftButton;
    @FXML
    private Button homeRightButton;

    @FXML
    private ComboBox<String> searchByNameBox;
    @FXML
    private ComboBox<String> searchByGenreBox;
    @FXML
    private ComboBox<String> searchByCinemaBox;

    @FXML
    private DatePicker searchByDatePicker;

    private List<Movie> movies = new ArrayList<>();
    private List<SoonMovie> soonMovies;
    private List<HomeMovie> homeMovies = new ArrayList<>();/////////////////////////////////////

    private List<Movie> filteredCinemaMovies = movies; /////////////////////////////
    private List<HomeMovie> filteredHomeMovies = homeMovies; ////////////////////////////////////////////

    private int cinemaCurrentIndex = 0;
    private int homeCurrentIndex = 0;

    private int filteredCinemaCurrentIndex = 0;
    private int filteredHomeCurrentIndex = 0;

    public void initialize() {
        EventBus.getDefault().register(this);
        requestMoviesFromServer();
        requestSoonMoviesFromServer();
        requestHomeMoviesFromServer();

        // הוספת אפשרויות לתיבת הז'אנרים
        searchByGenreBox.getItems().addAll("All", "Action", "Adventure", "Comedy", "Drama", "Documentary");

        searchByCinemaBox.getItems().addAll("All", "Haifa Cinema", "Tel Aviv Cinema", "Eilat Cinema", "Karmiel Cinema", "Jerusalem Cinema");

        searchByDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);  // If the date is before today it will not be available for selection
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                }
            }
        });

        searchByNameBox.setOnAction(event -> {        // מאזין לבחירת שם סרט
            String selectedMovieName = searchByNameBox.getValue();
            if (selectedMovieName != null) {
                Movie selectedMovie = findMovieByName(selectedMovieName);
                if (selectedMovie != null) {
                    openMovieDetailsPage(selectedMovie);
                }
            }
        });

        searchByGenreBox.setOnAction(event -> {      // מאזין לבחירת ז'אנר
            String selectedGenre = searchByGenreBox.getValue();
            filterMoviesByGenre(selectedGenre);  //  קריאה לפונקציה לסינון הסרטים ךפי הסוג

        });
        searchByCinemaBox.setOnAction(event -> {     //מאיזין לבחירת בית קלנוע
            String selectedCinema = searchByCinemaBox.getValue();
            filterMoviesByCinema(selectedCinema);        // קריאה לפונקציה לסינון  הסרטים לפי בית הקלנוע
        });

        cinemaImageView1.setOnMouseClicked(event -> openMovieDetailsPage(filteredCinemaMovies.get(filteredCinemaCurrentIndex)));
        cinemaLabel1.setOnMouseClicked(event -> openMovieDetailsPage(filteredCinemaMovies.get(filteredCinemaCurrentIndex)));


        cinemaImageView2.setOnMouseClicked(event -> openMovieDetailsPage(filteredCinemaMovies.get(filteredCinemaCurrentIndex + 1)));
        cinemaLabel2.setOnMouseClicked(event -> openMovieDetailsPage(filteredCinemaMovies.get(filteredCinemaCurrentIndex + 1)));

        cinemaImageView3.setOnMouseClicked(event -> openMovieDetailsPage(filteredCinemaMovies.get(filteredCinemaCurrentIndex + 2)));
        cinemaLabel3.setOnMouseClicked(event -> openMovieDetailsPage(filteredCinemaMovies.get(filteredCinemaCurrentIndex + 2)));

        cinemaImageView4.setOnMouseClicked(event -> openMovieDetailsPage(filteredCinemaMovies.get(filteredCinemaCurrentIndex + 3)));
        cinemaLabel4.setOnMouseClicked(event -> openMovieDetailsPage(filteredCinemaMovies.get(filteredCinemaCurrentIndex + 3)));

        homeImageView1.setOnMouseClicked(event -> openMovieDetailsPage(filteredHomeMovies.get(filteredHomeCurrentIndex)));
        homeLabel1.setOnMouseClicked(event -> openMovieDetailsPage(filteredHomeMovies.get(filteredHomeCurrentIndex)));

        homeImageView2.setOnMouseClicked(event -> openMovieDetailsPage(filteredHomeMovies.get(filteredHomeCurrentIndex + 1)));
        homeLabel2.setOnMouseClicked(event -> openMovieDetailsPage(filteredHomeMovies.get(filteredHomeCurrentIndex + 1)));

        homeImageView3.setOnMouseClicked(event -> openMovieDetailsPage(filteredHomeMovies.get(filteredHomeCurrentIndex + 2)));
        homeLabel3.setOnMouseClicked(event -> openMovieDetailsPage(filteredHomeMovies.get(filteredHomeCurrentIndex + 2)));

        homeImageView4.setOnMouseClicked(event -> openMovieDetailsPage(filteredHomeMovies.get(filteredHomeCurrentIndex + 3)));
        homeLabel4.setOnMouseClicked(event -> openMovieDetailsPage(filteredHomeMovies.get(filteredHomeCurrentIndex + 3)));
    }

    @Subscribe
    public void onUpdateMoviesEvent(UpdateMoviesEvent event) {
        Platform.runLater(() -> updateMovies(event.getMovies()));
    }

    @Subscribe
    public void onUpdateSoonMoviesEvent(UpdateSoonMoviesEvent event) {
        Platform.runLater(() -> {
            this.soonMovies = event.getSoonMovies();
            updateSoonMovies();
        });
    }

    @Subscribe
    public void onUpdateHomeMoviesEvent(UpdateHomeMoviesEvent event) {
        Platform.runLater(() -> {
            this.homeMovies = event.getHomeMovies();
            this.filteredHomeMovies = event.getHomeMovies(); ////////////////////
            updateHomeMovies();
        });
    }

    private void requestMoviesFromServer() {
        try {
            NewMessage message = new NewMessage("moviesList");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestSoonMoviesFromServer() {
        try {
            NewMessage message = new NewMessage("soonMoviesList");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestHomeMoviesFromServer() {
        try {
            NewMessage message = new NewMessage("homeMoviesList");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateHomeMovies() {
        if (homeMovies != null && homeMovies.size() >= 4) {
            homeImageView1.setImage(new Image(new ByteArrayInputStream(homeMovies.get(homeCurrentIndex).getImageData())));
            homeLabel1.setText(homeMovies.get(homeCurrentIndex).getHebtitle());

            homeImageView2.setImage(new Image(new ByteArrayInputStream(homeMovies.get(homeCurrentIndex + 1).getImageData())));
            homeLabel2.setText(homeMovies.get(homeCurrentIndex + 1).getHebtitle());

            homeImageView3.setImage(new Image(new ByteArrayInputStream(homeMovies.get(homeCurrentIndex + 2).getImageData())));
            homeLabel3.setText(homeMovies.get(homeCurrentIndex + 2).getHebtitle());

            homeImageView4.setImage(new Image(new ByteArrayInputStream(homeMovies.get(homeCurrentIndex + 3).getImageData())));
            homeLabel4.setText(homeMovies.get(homeCurrentIndex + 3).getHebtitle());
        }
    }

    private void updateImages() {
        if (movies != null && movies.size() >= 10) {
            cinemaImageView1.setImage(new Image(new ByteArrayInputStream(movies.get(cinemaCurrentIndex).getImageData())));
            cinemaLabel1.setText(movies.get(cinemaCurrentIndex).getHebtitle());

            cinemaImageView2.setImage(new Image(new ByteArrayInputStream(movies.get(cinemaCurrentIndex + 1).getImageData())));
            cinemaLabel2.setText(movies.get(cinemaCurrentIndex + 1).getHebtitle());

            cinemaImageView3.setImage(new Image(new ByteArrayInputStream(movies.get(cinemaCurrentIndex + 2).getImageData())));
            cinemaLabel3.setText(movies.get(cinemaCurrentIndex + 2).getHebtitle());

            cinemaImageView4.setImage(new Image(new ByteArrayInputStream(movies.get(cinemaCurrentIndex + 3).getImageData())));
            cinemaLabel4.setText(movies.get(cinemaCurrentIndex + 3).getHebtitle());
        }
    }

    private void updateMovieView(ImageView imageView, Label label, Movie movie) {
        imageView.setImage(new Image(new ByteArrayInputStream(movie.getImageData())));
        label.setText(movie.getHebtitle());
    }

    public void updateMovies(List<Movie> movies) {
        int flag = 0;
        for (Movie movie : movies){
            for(Branch branch : movie.getBranches()){
                if(branch.getName().equals("Haifa Cinema") || branch.getName().equals("Tel Aviv Cinema") ||
                        branch.getName().equals("Eilat Cinema") || branch.getName().equals("Karmiel Cinema")
                        || branch.getName().equals("Jerusalem Cinema") ){
                    flag = 1;
                }
            }
            if(flag == 1){
                this.movies.add(movie);
                flag = 0;
            }
        }
        updateImages();
        populateSearchByNameBox(); //to add the movies name to the search box
    }

    @FXML
    private void handleLeftButton() {
        if (filteredCinemaMovies != null && filteredCinemaMovies.size() > 0) {
            if (filteredCinemaCurrentIndex > 0) {
                filteredCinemaCurrentIndex--;
                updateFilteredCinemaMovies();
                //updateFilteredMovies(filteredCinemaMovies, filteredHomeMovies);
            }
        } /*else {
            if (cinemaCurrentIndex > 0) {
                cinemaCurrentIndex--;
                updateImages();
            }
        }
         */
    }

    @FXML
    private void handleRightButton() {
        if (filteredCinemaMovies != null && filteredCinemaMovies.size() > 0) {
            if (filteredCinemaCurrentIndex < filteredCinemaMovies.size() - 4) {
                filteredCinemaCurrentIndex++;
                updateFilteredCinemaMovies();
                //updateFilteredMovies(filteredCinemaMovies, filteredHomeMovies);
            }
        } /*else {
            if (movies != null && cinemaCurrentIndex < movies.size() - 4) {
                cinemaCurrentIndex++;
                updateImages();
            }
        }
        */
    }

    @FXML
    private void handleHomeLeftButton() {
        if (filteredHomeMovies != null && filteredHomeMovies.size() > 0) {
            if (filteredHomeCurrentIndex > 0) {
                filteredHomeCurrentIndex--;
                updateFilteredHomeMovies();
                //updateFilteredMovies(filteredCinemaMovies, filteredHomeMovies);
            }
        }/* else {
            if (homeCurrentIndex > 0) {
                homeCurrentIndex--;
                updateHomeMovies();
            }
        }
        */
    }

    @FXML
    private void handleHomeRightButton() {
        if (filteredHomeMovies != null && filteredHomeMovies.size() > 0) {
            if (filteredHomeCurrentIndex < filteredHomeMovies.size() - 4) {
                filteredHomeCurrentIndex++;
                updateFilteredHomeMovies();
                //updateFilteredMovies(filteredCinemaMovies, filteredHomeMovies);
            }
        } /*else {
            if (homeMovies != null && homeCurrentIndex < homeMovies.size() - 4) {
                homeCurrentIndex++;
                updateHomeMovies();
            }
        }
        */
    }

    private void updateSoonMovies() {
        if (soonMovies != null && soonMovies.size() == 3) {
            soonImageView1.setImage(new Image(new ByteArrayInputStream(soonMovies.get(0).getImageData())));
            soonLabel1.setText(soonMovies.get(0).getHebtitle());

            soonImageView2.setImage(new Image(new ByteArrayInputStream(soonMovies.get(1).getImageData())));
            soonLabel2.setText(soonMovies.get(1).getHebtitle());

            soonImageView3.setImage(new Image(new ByteArrayInputStream(soonMovies.get(2).getImageData())));
            soonLabel3.setText(soonMovies.get(2).getHebtitle());
        }
    }

    // we call this method after the movies updated to add options to  searchByNameBox
    private void populateSearchByNameBox() {
        if (movies != null) {
            searchByNameBox.getItems().clear();// Clear existing items
            searchByNameBox.getItems().add("All");
            for (Movie movie : movies) {
                searchByNameBox.getItems().add(movie.getEngtitle());
            }
        }
    }

    private void filterMoviesByGenre(String selectedGenre) {
        if(searchByCinemaBox.getValue() == null) {
            searchByCinemaBox.setValue("All");
        }
        if(!selectedGenre.equals("All")) {
            if (movies != null && !selectedGenre.equals("All")) {
                // סינון סרטים המוקרנים בקולנוע לפי הז'אנר ושאינם מסוג
                filteredCinemaMovies = movies.stream()
                        .filter(movie -> movie.getGenre().equalsIgnoreCase(selectedGenre))
                        .filter(movie -> !(movie instanceof HomeMovie))
                        .filter(movie -> (searchByCinemaBox.getValue().equals("All") || movie.getBranches().stream() /////////////
                                .anyMatch(branch -> branch.getName().equalsIgnoreCase(searchByCinemaBox.getValue())))) //////////////
                        .collect(Collectors.toList());
                if (!searchByCinemaBox.getValue().equals("All")) {
                    NowInCinema.setText(selectedGenre + " movies in " + searchByCinemaBox.getValue());///////////////////////////
                } else
                    NowInCinema.setText(selectedGenre + " movies in the Cinema");////////////////////////////
            }

            if (homeMovies != null && !selectedGenre.equals("All")) {
                filteredHomeMovies = homeMovies.stream()
                        .filter(movie -> movie.getGenre().equalsIgnoreCase(selectedGenre))
                        .collect(Collectors.toList());
                WatchFromHome.setText(selectedGenre + " movies to watch from home");
            }
        }

        else if (!searchByCinemaBox.getValue().equals("All")){
            filterMoviesByCinema(searchByCinemaBox.getValue());
            filteredHomeMovies = new ArrayList<>(homeMovies);
            WatchFromHome.setText("Watch from home");
        }
        else if(searchByCinemaBox.getValue().equals("All") && searchByCinemaBox.getValue().equals("All")){
            filteredCinemaMovies = new ArrayList<>(movies);
            NowInCinema.setText("Now in the Cinema");
            filteredHomeMovies = new ArrayList<>(homeMovies);
            WatchFromHome.setText("Watch from home");
        }

        // אפס את האינדקסים אחרי הסינון כדי להראות את הסרטים הראשונים
        filteredCinemaCurrentIndex = 0;
        filteredHomeCurrentIndex = 0;

        updateFilteredCinemaMovies();
        updateFilteredHomeMovies();
        //updateFilteredMovies(filteredCinemaMovies, filteredHomeMovies);
    }

    /*

    private void updateFilteredMovies(List<Movie> filteredCinemaMovies, List<HomeMovie> filteredHomeMovies) {
        clearCinemaDisplay();
        clearHomeDisplay();

        if (!filteredCinemaMovies.isEmpty()) {
            for (int i = 0; i < Math.min(filteredCinemaMovies.size(), 4); i++) {
                updateMovieView(getCinemaImageView(i), getCinemaLabel(i), filteredCinemaMovies.get(i));
            }
        }
        if (!filteredHomeMovies.isEmpty()) {
            for (int i = 0; i < Math.min(filteredHomeMovies.size(), 4); i++) {
                updateMovieView(getHomeImageView(i), getHomeLabel(i), filteredHomeMovies.get(i));
            }
        }
    }

     */
    /* ////////////////////////////////////////////////////////////////
    private void updateFilteredMovies(List<Movie> filteredCinemaMovies, List<HomeMovie> filteredHomeMovies) {
        clearCinemaDisplay();

        if (!filteredCinemaMovies.isEmpty()) {
            for (int i = 0; i < Math.min(filteredCinemaMovies.size(), 4); i++) {
                int displayIndex = filteredCinemaCurrentIndex + i; // Offset by current index
                if (displayIndex < filteredCinemaMovies.size()) {
                    updateMovieView(getCinemaImageView(i), getCinemaLabel(i), filteredCinemaMovies.get(displayIndex));
                }
            }
        }

        clearHomeDisplay();

        if (!filteredHomeMovies.isEmpty()) {
            for (int i = 0; i < Math.min(filteredHomeMovies.size(), 4); i++) {
                int displayIndex = filteredHomeCurrentIndex + i;
                if (displayIndex < filteredHomeMovies.size()) {
                    updateMovieView(getHomeImageView(i), getHomeLabel(i), filteredHomeMovies.get(displayIndex));
                }
            }
        }
    }

     */
    private void updateFilteredCinemaMovies() {
        clearCinemaDisplay();

        if (!filteredCinemaMovies.isEmpty()) {
            for (int i = 0; i < Math.min(filteredCinemaMovies.size(), 4); i++) {
                int displayIndex = filteredCinemaCurrentIndex + i;
                if (displayIndex < filteredCinemaMovies.size()) {
                    updateMovieView(getCinemaImageView(i), getCinemaLabel(i), filteredCinemaMovies.get(displayIndex));
                }
            }
        }

        // if (Objects.equals(searchByCinemaBox.getValue(), "All")) {/////////////////////////////////////
        //   NowInCinema.setText("Now in the cinema");
        //} else {
        //  NowInCinema.setText("Now in " + searchByCinemaBox.getValue());
        // }
    }

    private void updateFilteredHomeMovies() {
        clearHomeDisplay();

        if (!filteredHomeMovies.isEmpty()) {
            for (int i = 0; i < Math.min(filteredHomeMovies.size(), 4); i++) {
                int displayIndex = filteredHomeCurrentIndex + i;
                if (displayIndex < filteredHomeMovies.size()) {
                    updateMovieView(getHomeImageView(i), getHomeLabel(i), filteredHomeMovies.get(displayIndex));
                }
            }
        }
    }


    private ImageView getCinemaImageView(int index) {
        switch (index) {
            case 0: return cinemaImageView1;
            case 1: return cinemaImageView2;
            case 2: return cinemaImageView3;
            case 3: return cinemaImageView4;
            default: return null;
        }
    }

    private Label getCinemaLabel(int index) {
        switch (index) {
            case 0: return cinemaLabel1;
            case 1: return cinemaLabel2;
            case 2: return cinemaLabel3;
            case 3: return cinemaLabel4;
            default: return null;
        }
    }

    private ImageView getHomeImageView(int index) {
        switch (index) {
            case 0: return homeImageView1;
            case 1: return homeImageView2;
            case 2: return homeImageView3;
            case 3: return homeImageView4;
            default: return null;
        }
    }

    private Label getHomeLabel(int index) {
        switch (index) {
            case 0: return homeLabel1;
            case 1: return homeLabel2;
            case 2: return homeLabel3;
            case 3: return homeLabel4;
            default: return null;
        }
    }


    private void clearCinemaDisplay() {
        cinemaImageView1.setImage(null);
        cinemaLabel1.setText("");
        cinemaImageView2.setImage(null);
        cinemaLabel2.setText("");
        cinemaImageView3.setImage(null);
        cinemaLabel3.setText("");
        cinemaImageView4.setImage(null);
        cinemaLabel4.setText("");
    }

    private void clearHomeDisplay() {
        homeImageView1.setImage(null);
        homeLabel1.setText("");
        homeImageView2.setImage(null);
        homeLabel2.setText("");
        homeImageView3.setImage(null);
        homeLabel3.setText("");
        homeImageView4.setImage(null);
        homeLabel4.setText("");
    }


    private void filterMoviesByCinema(String selectedCinema) {
        if (movies != null && !selectedCinema.equals("All")) {
            if(searchByGenreBox.getValue() == null) {
                searchByGenreBox.setValue("All");
            }
     /*    filteredCinemaMovies = movies.stream()
                 .filter(movie -> movie.getBranches().stream()
                         .anyMatch(branch -> branch.getName().equalsIgnoreCase(selectedCinema)))
                 .collect(Collectors.toList()); */
            filteredCinemaMovies = movies.stream()
                    .filter(movie -> (searchByGenreBox.getValue().equals("All") || movie.getGenre().equalsIgnoreCase(searchByGenreBox.getValue())))
                    .filter(movie -> (movie.getBranches().stream()
                            .anyMatch(branch -> branch.getName().equalsIgnoreCase(selectedCinema))))
                    .collect(Collectors.toList());

            if(!searchByGenreBox.getValue().equals("All")){
                NowInCinema.setText(searchByGenreBox.getValue() + " movies in " + selectedCinema);///////////////////////////
            }
            else
                NowInCinema.setText("Now in " + selectedCinema);////////////////////////////
        }
        else if(searchByGenreBox.getValue().equals("All")){
            filteredCinemaMovies = new ArrayList<>(movies);
            NowInCinema.setText("Now in the Cinema");
        }

        else {
            //  filteredCinemaMovies = new ArrayList<>(movies);
            // NowInCinema.setText("Now in the Cinema");
            filterMoviesByGenre(searchByGenreBox.getValue());
        }
        // איפוס אינדקס כדי להתחיל מהסרט הראשון
        filteredCinemaCurrentIndex = 0;
        //  filteredHomeMovies = new ArrayList<>(homeMovies);
        // filteredHomeCurrentIndex = 0;

        updateFilteredCinemaMovies();

    }
/*
    private void updateFilteredCinemaMovies() {
     clearCinemaDisplay(); // ניקוי התצוגה הקודמת

     if (!filteredCinemaMovies.isEmpty()) {
       for (int i = 0; i < Math.min(filteredCinemaMovies.size(), 4); i++) {
           updateMovieView(getCinemaImageView(i), getCinemaLabel(i), filteredCinemaMovies.get(i));
      }
     }
     if(Objects.equals(searchByCinemaBox.getValue(), "All")){
         NowInCinema.setText("Now in the cinema");
     }
      else NowInCinema.setText("Now in " + searchByCinemaBox.getValue());
     }

 */

    @FXML
    private void handleSearch() {
        String name = searchByNameBox.getValue();
        String genre = searchByGenreBox.getValue();
        String cinema = searchByCinemaBox.getValue();
        LocalDate date = searchByDatePicker.getValue();
    }

    private Movie findMovieByName(String movieName) {   //if we want to search by name
        for (Movie movie : movies) {
            if (movie.getEngtitle().equalsIgnoreCase(movieName)) {
                return movie;
            }
        }
        return null;
    }

    private void openMovieDetailsPage(Movie movie) {
        MovieDetailsPage.setSelectedMovie(movie);  // שמור את הסרט שנבחר

        App.switchScreen("MovieDetailsPage");  // מעבר לדף
    }


    @FXML
    private void switchToHomePage() throws IOException {
        App.switchScreen("HomePage");
    }

    @FXML
    private void switchToComplaintPage() throws IOException {
        App.switchScreen("ComplaintPage");
    }

    @FXML
    private void switchToLoginPage() throws IOException {
        App.switchScreen("LoginPage");
    }

    @FXML
    private void switchToHostPage() throws IOException {
        App.switchScreen("HostPage");
    }

    @FXML
    public void switchToMoviesPage() throws IOException {
        App.switchScreen("MoviesPage");
    }

    @FXML
    private void switchToChargebackPage() throws IOException {
        App.switchScreen("ChargebackPage");
    }

    @FXML
    private void switchToCardsPage() throws IOException {
        App.switchScreen("CardsPage");
    }
}