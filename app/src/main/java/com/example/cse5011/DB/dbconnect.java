package com.example.cse5011.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cse5011.R;
import com.example.cse5011.model.CartItem;
import com.example.cse5011.model.Product;
import com.example.cse5011.model.users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class dbconnect extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Printexpress";
    private static final int DATABASE_VERSION = 4;

    // Users Table
    private static final String TABLE_USERS = "users";
    private static final String U_ID = "id";
    private static final String U_NAME = "name";
    private static final String U_EMAIL = "email";
    private static final String U_PASSWORD = "password";

    // Products Table
    private static final String TABLE_PRODUCTS = "products";
    private static final String P_ID = "id";
    private static final String P_TITLE = "title";
    private static final String P_DESC = "description";
    private static final String P_PRICE = "price";
    private static final String P_IMAGE = "image_res";

    // Cart Table
    private static final String TABLE_CART = "cart";
    private static final String C_ID = "id";
    private static final String C_TITLE = "title";
    private static final String C_PRICE = "price";
    private static final String C_SIZE = "size";
    private static final String C_QUANTITY = "quantity";
    private static final String C_IMAGE = "image_res";

    // Orders / Purchase History Table
    private static final String TABLE_ORDERS = "orders";
    private static final String O_ID = "id";
    private static final String O_TITLE = "title";
    private static final String O_PRICE = "price";
    private static final String O_SIZE = "size";
    private static final String O_QUANTITY = "quantity";
    private static final String O_DATE = "date";
    private static final String O_IMAGE = "image_res";

    public dbconnect(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + U_NAME + " TEXT,"
                + U_EMAIL + " TEXT,"
                + U_PASSWORD + " TEXT" + ")";

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + P_TITLE + " TEXT,"
                + P_DESC + " TEXT,"
                + P_PRICE + " TEXT,"
                + P_IMAGE + " INTEGER" + ")";

        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + C_TITLE + " TEXT,"
                + C_PRICE + " TEXT,"
                + C_SIZE + " TEXT,"
                + C_QUANTITY + " INTEGER,"
                + C_IMAGE + " INTEGER" + ")";

        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + O_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + O_TITLE + " TEXT,"
                + O_PRICE + " TEXT,"
                + O_SIZE + " TEXT,"
                + O_QUANTITY + " INTEGER,"
                + O_DATE + " TEXT,"
                + O_IMAGE + " INTEGER" + ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_ORDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    // --- Users Operations ---
    public void addUser(users user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(U_NAME, user.getName());
        values.put(U_EMAIL, user.getEmail());
        values.put(U_PASSWORD, user.getPassword());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE (" + U_NAME + "=? OR " + U_EMAIL + "=?) AND " + U_PASSWORD + "=?",
                new String[]{username, username, password}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // --- Products Operations ---
    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(P_TITLE, product.getTitle());
        values.put(P_DESC, product.getDescription());
        values.put(P_PRICE, product.getPrice());
        values.put(P_IMAGE, product.getImageRes());
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(P_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(P_TITLE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(P_DESC));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(P_PRICE));
                int imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(P_IMAGE));

                productList.add(new Product(id, title, desc, price, imageRes));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return productList;
    }

    // --- Cart Operations ---
    public void addToCart(String title, String price, String size, int quantity, int imageRes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_TITLE, title);
        values.put(C_PRICE, price);
        values.put(C_SIZE, size);
        values.put(C_QUANTITY, quantity);
        values.put(C_IMAGE, imageRes);
        db.insert(TABLE_CART, null, values);
        db.close();
    }

    public List<CartItem> getCartItems() {
        List<CartItem> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(C_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(C_TITLE));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(C_PRICE));
                String size = cursor.getString(cursor.getColumnIndexOrThrow(C_SIZE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(C_QUANTITY));
                int imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(C_IMAGE));

                itemList.add(new CartItem(id, title, price, size, quantity, imageRes));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemList;
    }

    public void deleteCartItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, C_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
        db.close();
    }

    // --- Purchase History / Orders Operations ---
    public boolean checkoutCart() {
        List<CartItem> cartItems = getCartItems();
        if (cartItems.isEmpty()) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        for (CartItem item : cartItems) {
            ContentValues values = new ContentValues();
            values.put(O_TITLE, item.getTitle());
            values.put(O_PRICE, item.getPrice());
            values.put(O_SIZE, item.getSize());
            values.put(O_QUANTITY, item.getQuantity());
            values.put(O_DATE, currentDate);
            values.put(O_IMAGE, item.getImageRes());
            db.insert(TABLE_ORDERS, null, values);
        }

        db.delete(TABLE_CART, null, null);
        db.close();
        return true;
    }

    public List<CartItem> getPurchaseHistory() {
        List<CartItem> historyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ORDERS + " ORDER BY " + O_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(O_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(O_TITLE));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(O_PRICE));
                String size = cursor.getString(cursor.getColumnIndexOrThrow(O_SIZE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(O_QUANTITY));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(O_DATE));
                int imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(O_IMAGE));

                historyList.add(new CartItem(id, title, price, size, quantity, imageRes, date));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return historyList;
    }

    // Helper to insert sample product data if empty
    public void insertSampleProductsIfEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PRODUCTS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();

        if (count == 0) {
            addProduct(new Product("Custom T-Shirt", "High quality cotton printed T-shirt with customizable graphics", "$15.99", R.mipmap.ic_launcher));
            addProduct(new Product("Printed Ceramic Mug", "11oz ceramic mug with vivid custom color print", "$8.99", R.mipmap.ic_mug));
            addProduct(new Product("Business Cards", "100 pcs premium matte finish custom business cards", "$12.50", R.mipmap.ic_businesscard));
            addProduct(new Product("Custom Poster Print", "A3 glossy photo paper high resolution print", "$6.00", R.mipmap.ic_poster));
            addProduct(new Product("Custom Hoodie", "Fleece pullover hoodie with custom chest print", "$34.99", R.mipmap.ic_hoodie));
        }
    }
}
