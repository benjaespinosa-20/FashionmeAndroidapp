package mx.app.fashionme.restApi.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import mx.app.fashionme.pojo.Category;
import mx.app.fashionme.restApi.JsonKeys;
import mx.app.fashionme.restApi.model.CategoryResponse;

/**
 * Created by heriberto on 13/03/18.
 */

public class CategoryDeserializer implements JsonDeserializer<CategoryResponse>{

    @Override
    public CategoryResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        CategoryResponse categoryResponse = gson.fromJson(json, CategoryResponse.class);
        JsonArray categoryResponseData = json.getAsJsonObject().getAsJsonArray(JsonKeys.RESPONSES_ARRAY_DATA);

        categoryResponse.setCategories(deserializarCategoriaDeJson(categoryResponseData));

        return categoryResponse;
    }

    private ArrayList<Category> deserializarCategoriaDeJson(JsonArray categoryResponseData) {
        ArrayList<Category> categories = new ArrayList<>();
        for (int i = 0; i <categoryResponseData.size() ; i++) {
            JsonObject dataObject = categoryResponseData.get(i).getAsJsonObject();

            int id = dataObject.get(JsonKeys.CATEGORY_ID).getAsInt();
            String name = dataObject.get(JsonKeys.CATEGORY_NAME).getAsString();
            String genre = dataObject.get(JsonKeys.CATEGORY_GENRE).getAsString();

            String urlImage = dataObject.get(JsonKeys.CATEGORY_URL).getAsString();
            if (urlImage.equals("")){
                urlImage = null;
            }
            //String urlImage = dataObject.get(JsonKeys.CATEGORY_URL).getAsString();
            Category categoryActual = new Category();
            categoryActual.setId(id);
            //categoryActual.setName(name);
            categoryActual.setGenre(genre);
            categoryActual.setUrlImage(urlImage);

            categories.add(categoryActual);
        }

        return categories;
    }

}
