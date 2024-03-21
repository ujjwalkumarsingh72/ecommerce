package com.example.ecommerce;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class UserInterface {
    GridPane loginpage;
    HBox headerBar;
    HBox footerBar;
    Button signInButton;
    //Button cartButton;

    Label welcomeLabel;
    VBox body;
    ProductList productList = new ProductList();
    VBox productPage;

    Button placeOrderButton= new Button ("place Order");
    ObservableList<Product> itemsInCart = FXCollections.observableArrayList();
    Customer loggedInCustomer;
    public BorderPane createContent(){
        BorderPane root= new BorderPane();
        root.setPrefSize(800,600);

        //root.getChildren().add(loginpage);
        root.setTop(headerBar);
        //root.setCenter(loginpage);
        body = new VBox();
        body.setPadding(new Insets(10));
        body.setAlignment(Pos.CENTER);
        root.setCenter(body);
        productPage= productList.getALLProducts();
        body.getChildren().add(productPage);

        root.setBottom(footerBar);
        //root.setCenter(productPage);
        return root;
    }

    public UserInterface (){
        createLoginPage();
        createHeaderBar();
        createFooterBar();
    }

    private void createLoginPage(){
        Text userNameText= new Text("User Name");
        Text passwordText= new Text("Password");

        TextField userName = new TextField();
        userName.setPromptText("Type your user name here");
        PasswordField password = new PasswordField();
        password.setPromptText("Type your password here");

        Label messageLabel = new Label("Hi");
        Button loginButton = new Button("Login");

        loginpage= new GridPane();
        loginpage.setAlignment(Pos.CENTER);
        loginpage.setHgap(10);
        loginpage.setVgap(10);
        loginpage.add(userNameText, 0,0);
        loginpage.add(userName, 1,0);
        loginpage.add(passwordText, 0,1);
        loginpage.add(password, 1,1);
        loginpage.add(messageLabel, 0, 2);
        loginpage.add(loginButton,1,2);

        loginButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent) {
                String name = userName.getText();
                String pass = password.getText();
                Login login = new Login();
                loggedInCustomer = login.customerLogin(name, pass);

                if (loggedInCustomer != null) {
                    messageLabel.setText("Welcome:" + loggedInCustomer.getName());
                    welcomeLabel.setText("Welcome:" + loggedInCustomer.getName());
                    headerBar.getChildren().add(welcomeLabel);
                    body.getChildren().clear();
                    body.getChildren().add(productPage);

                } else {
                    messageLabel.setText("LogIn Failed ! please provide correct credentials") ;
                }
            }
        });

    }

    private void createHeaderBar() {

        Button homeButton = new Button();
        Image image = new Image("C:\\Users\\ujjwa\\IdeaProjects\\snake_ladder\\src\\main\\resources\\ecommerce-business.jpg");
        ImageView imageView= new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(80);
        homeButton.setGraphic(imageView);

        TextField searchBar = new TextField();
        searchBar.setPromptText("search here");
        searchBar.setPrefWidth(280);
        Button searchButton = new Button("Search");
        signInButton = new Button("Sign In");
        welcomeLabel = new Label();

        Button cartButton= new Button("Cart");

        headerBar = new HBox();
        headerBar.setPadding(new Insets(10));
        headerBar.setSpacing(10);
        headerBar.setAlignment(Pos.CENTER);
        headerBar.getChildren().addAll(homeButton, searchBar, searchButton, signInButton, cartButton);

        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                body.getChildren().add(loginpage);
                headerBar.getChildren().remove(signInButton);
            }
        });


        cartButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent){
                body.getChildren().clear();
                VBox prodPage = productList.getProductsInCart(itemsInCart);
                prodPage.setAlignment(Pos.CENTER);
                prodPage.setSpacing(10);
                prodPage.getChildren().add(placeOrderButton);
                body.getChildren().add(prodPage);
                footerBar.setVisible(false);
            }
        });

        placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                if(product== null)
                {
                    showDialog("Please select a product first to place order! ");
                    return;
                }
                if( loggedInCustomer ==null)
                {
                    showDialog("please login first to place order");
                }

                int count = Order.placeMultipleOrder(loggedInCustomer, itemsInCart);
                if(count!= 0){
                    showDialog("Order for "+count+" products placed successfully!!");
                }

                else{
                    showDialog("Order failed");
                }
            }
        });

        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                body.getChildren().add(productPage);
                footerBar.setVisible(true);
                if(loggedInCustomer==null)
                {
                    System.out.println(headerBar.getChildren().indexOf(signInButton));
                    if(headerBar.getChildren().indexOf(signInButton)==-1){
                        headerBar.getChildren().add(signInButton);
                    }
                }

            }
        });







    }


    private void createFooterBar() {


        Button buyNowButton = new Button("BuyNow");
        Button addToCartButton= new Button ("Add to Cart");

        footerBar = new HBox();
        footerBar.setPadding(new Insets(10));
        footerBar.setSpacing(10);
        footerBar.setAlignment(Pos.CENTER);
        footerBar.getChildren().addAll(buyNowButton, addToCartButton);

        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                if(product== null)
                {
                    showDialog("Please select a product first to place order! ");
                    return;
                }
                if( loggedInCustomer ==null)
                {
                    showDialog("please login first to place order");
                }

                boolean status = Order.placeOrder(loggedInCustomer, product);
                if(status== true){
                    showDialog("order placed successfully!!");
                }

                else{
                    showDialog("Order failed");
                }
            }
        });

    addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            Product product = productList.getSelectedProduct();
            if(product==null){
                showDialog("Please select a product first to add it to Cart!");
                return;
            }
            itemsInCart.add(product);
            showDialog("Selected item has been added to the cart successfully!");
        }
    });

    //homeButton.setOn

    }
    private void showDialog(String message){
        Alert alert= new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setTitle("message");
        alert.showAndWait();
    }


}
