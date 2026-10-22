# PrintExpress 🖨️📱

**PrintExpress** is a native Android e-commerce application built using Java, SQLite, and Material Design 3. The app allows users to browse custom printable products (T-shirts, mugs, business cards, posters, hoodies), customize sizes/quantities, manage a persistent shopping cart, and track order fulfillment in real-time.

---

## 🌟 Key Features

* **User Authentication:** Login and registration system powered by SQLite database storage.
* **Dynamic Product Catalog:** Interactive product feed rendered via `RecyclerView` with live search filtering (`SearchView`) by title and description.
* **Product Detail & Customization View:**
  * Multi-image sample carousel driven by `ViewPager2`.
  * Material `ChipGroup` size/format selection (`Small`, `Medium`, `Large`, `XL`).
  * Quantity selector controls (`-` / `+`).
* **Persistent SQLite Shopping Cart:** Add/remove items, manage quantities, and view dynamic cart total calculations.
* **Payment & Order Progress Tracker:**
  * Multi-option checkout form supporting Card and Cash on Delivery (COD).
  * Visual 4-step order progress indicator (*Order Placed $\rightarrow$ Printing $\rightarrow$ Shipped $\rightarrow$ Delivered*).
  * Persistent purchase history log in SQLite.

---

## 🛠️ Tech Stack & Libraries

* **Language:** Java
* **Platform:** Android SDK (API 24 - 37)
* **Database:** SQLite (`SQLiteOpenHelper`)
* **UI Components:** Material Design 3, `RecyclerView`, `ViewPager2`, `ChipGroup`, `ConstraintLayout`, `RelativeLayout`
* **Build System:** Gradle (Kotlin DSL)

---

## 🗄️ Database Architecture

The SQLite database (`Printexpress`) manages 4 relational tables:

1. **`users`**: Stores user authentication profiles (`id`, `name`, `email`, `password`).
2. **`products`**: Stores catalog items (`id`, `title`, `description`, `price`, `image_res`).
3. **`cart`**: Stores active shopping cart items (`id`, `title`, `price`, `size`, `quantity`, `image_res`).
4. **`orders`**: Stores completed purchase history (`id`, `title`, `price`, `size`, `quantity`, `date`, `image_res`).

---

## 🚀 Getting Started

### Prerequisites
* Android Studio Ladybug (or newer)
* Android SDK API Level 24+
* JDK 11+

### Installation & Run

1. Clone the repository:
   ```bash
   git clone https://github.com/<your-username>/CSE5011.git
   ```
2. Open the project in **Android Studio**.
3. Allow Gradle to sync dependencies.
4. Run the app on an Android Emulator or connected physical device.

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
