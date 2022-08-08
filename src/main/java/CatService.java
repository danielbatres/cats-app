import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
