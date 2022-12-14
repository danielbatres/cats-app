import com.google.gson.Gson;
import okhttp3.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class CatService {
    public static void seeCats() {
        // We'll get the cats from the API
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .get()
                .build();

        String json;

        try {
            Response response = client.newCall(request).execute();
            json = response.body().string();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // cut brackets

        json = json.substring(1, json.length());
        json = json.substring(0, json.length() -1);

        // create cat type object

        Gson gson = new Gson();
        Cats cats = gson.fromJson(json, Cats.class);

        // resize if needed

        Image image = null;

        try {
            URL url = new URL(cats.getUrl());

            image = ImageIO.read(url);

            ImageIcon catBackground = new ImageIcon(image);

            if (catBackground.getIconWidth() > 800) {
                Image background = catBackground.getImage();
                Image modified = background.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
            }

            String menu = "Options: \n" +
                    "1. See another image\n" +
                    "2. Favorite\n" +
                    "3. Back\n";

            String[] buttons = {"See another image", "Favorite", "Back"};
            String idCat = cats.getId();
            String option = (String) JOptionPane.showInputDialog(null, menu, idCat, JOptionPane.INFORMATION_MESSAGE, catBackground, buttons, buttons[0]);

            int selection = -1;

            for (int i = 0; i < buttons.length; i++) {
                if (option.equals(buttons[i])) {
                    selection = i;
                }
            }

            switch (selection) {
                case 0:
                    seeCats();
                    break;
                case 1:
                    favoriteCat(cats);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void favoriteCat(Cats cat) {
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n\t\"image_id\": \"" + cat.getId() + "\"\n}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", Private.apiKey)
                    .build();

            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void seeFavourites(String apiKey) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .get()
                    .addHeader("x-api-key", apiKey)
                    .build();

            Response response = client.newCall(request).execute();

            String json = response .body().string();

            // Create gson object

            Gson gson = new Gson();

            FavCats[] arrayCats = gson.fromJson(json, FavCats[].class);

            if (arrayCats.length > 0) {
                int min = 1;
                int max = arrayCats.length;
                int random = (int) (Math.random() * ((max -min) + 1)) + min;
                int index = random - 1;

                FavCats favCat = arrayCats[index];

                Image image = null;

                try {
                    URL url = new URL(favCat.image.getUrl());

                    image = ImageIO.read(url);

                    ImageIcon catBackground = new ImageIcon(image);

                    if (catBackground.getIconWidth() > 800) {
                        Image background = catBackground.getImage();
                        Image modified = background.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                    }

                    String menu = "Options: \n" +
                            "1. See another image\n" +
                            "2. Delete favourite\n" +
                            "3. Back\n";

                    String[] buttons = {"See another image", "Delete favorite", "Back"};
                    String idCat = favCat.getId();
                    String option = (String) JOptionPane.showInputDialog(null, menu, idCat, JOptionPane.INFORMATION_MESSAGE, catBackground, buttons, buttons[0]);

                    int selection = -1;

                    for (int i = 0; i < buttons.length; i++) {
                        if (option.equals(buttons[i])) {
                            selection = i;
                        }
                    }

                    switch (selection) {
                        case 0:
                            seeFavourites(apiKey);
                            break;
                        case 1:
                            deleteFavourite(favCat);
                            break;
                        default:
                            break;
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteFavourite(FavCats favCat) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/" + favCat.getId())
                    .delete(null)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", favCat.getApiKey())
                    .build();

            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
