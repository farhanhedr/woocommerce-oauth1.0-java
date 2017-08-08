public class Main {
    public static void main(String[] args) {
        String URL = "http://api.example.com/wp-json/wc/v2/products";
        String consumerKey = "ck_85680dc4xxxxxxxxxx";
        String consumerSecret = "cs_69be1614cxxxxxxxxxx";
        String response;

        Executer executer = new Executer();
        response = executer.execute(URL, consumerKey, consumerSecret);

        System.out.println(response);
    }
}
