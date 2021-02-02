package mx.app.fashionme.restApi.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import mx.app.fashionme.pojo.User;
import mx.app.fashionme.restApi.JsonKeys;
import mx.app.fashionme.restApi.model.UserResponse;

/**
 * Created by heriberto on 15/03/18.
 */

public class UserLoginDeserializer implements JsonDeserializer<UserResponse> {

    @Override
    public UserResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        UserResponse userResponse = gson.fromJson(json, UserResponse.class);
        JsonObject jsonObject = json.getAsJsonObject();
        //userResponse.setUser(deserializarUser(jsonObject));
        return userResponse;
    }

    private User deserializarUser(JsonObject jsonObject) {

        JsonObject jsonObjectData = jsonObject.getAsJsonObject().get(JsonKeys.RESPONSES_ARRAY_DATA).getAsJsonObject();

        JsonElement elementId = jsonObjectData.get(JsonKeys.USER_ID);
        int id = 0;
        if (!elementId.isJsonNull()) {
            id = elementId.getAsInt();
        }


        String name = jsonObjectData.get(JsonKeys.USER_NAME).getAsString();

        JsonElement jsonElementEmail = jsonObjectData.get(JsonKeys.USER_EMAIL);
        String email = null;
        if (!jsonElementEmail.isJsonNull()) {
            email = jsonElementEmail.getAsString();
        }

        JsonElement elementColor = jsonObjectData.get(JsonKeys.USER_COLOR);
        String color = null;
        if (!elementColor.isJsonNull()) {
            color = elementColor.getAsString();
        }

        JsonElement elementBody = jsonObjectData.get(JsonKeys.USER_BODY);
        String body = null;
        if (!elementBody.isJsonNull()) {
            body = jsonObjectData.get(JsonKeys.USER_BODY).getAsString();
        }

        JsonElement elementGenre = jsonObjectData.get(JsonKeys.USER_GENDER);
        String genre = null;
        if (!elementGenre.isJsonNull()) {
            genre = jsonObjectData.get(JsonKeys.USER_GENDER).getAsString();
        }

        JsonElement elementRole = jsonObjectData.get(JsonKeys.USER_ROLE);
        String role = null;
        if (!elementRole.isJsonNull()) {
            role = jsonObjectData.get(JsonKeys.USER_ROLE).getAsString();
        }

        String token = null;
        if (jsonObject.get(JsonKeys.USER_ACCESS_TOKEN) != null) {
            if (!jsonObject.get(JsonKeys.USER_ACCESS_TOKEN).isJsonNull()) {
                token = jsonObject.get(JsonKeys.USER_ACCESS_TOKEN).getAsString();
            }
        }

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        //user.setColor(color);
        user.setGenre(genre);
        user.setRole(role);
        //user.setBody(body);
        user.setToken(token);

        return user;
    }
}
