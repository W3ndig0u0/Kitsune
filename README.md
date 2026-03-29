
# Kitsune 🦊 

**Kitsune** is a high-performance, full-stack anime streaming platform. It seamlessly integrates real-time metadata from the AniList API with high-quality video scraping, providing users with a unified, feature-rich viewing experience.

## 🚀 Key Features

*   **Dual-Source Integration**: Dynamically balances data from the **AniList API** for metadata and a custom **Web Scraper** for AnimeKai video sources.
*   **User Persistence**: Robust account system featuring:
    *   **Watchlists**: Save what you want to see next.
    *   **Smart Resume**: Automatically saves your last watched episode and the exact timestamp so you can jump back in instantly.
    *   **Leveling System**: Gamified user experience where watching anime increases your profile rank.
*   **Security First**: Implements **JWT (JSON Web Tokens)** for session management and **BCrypt Password Hashing** for database security.
*   **Modern Tech Stack**: A reactive, single-page frontend paired with a scalable, relational backend.

## 🛠️ Technical Stack

*   **Frontend**: React.js (Hooks, Context/Redux, Responsive Design)
*   **Backend**: Java / Spring Boot (REST API, Security, Data JPA)
*   **Database**: PostgreSQL
*   **Security**: Spring Security + JWT
*   **Data**: AniList GraphQL API & Custom Web Scraper

## 📂 Project Structure

```text
├── kitsune-frontend/   # React application (UI/UX)
├── kitsune-backend/    # Spring Boot application (Logic & Auth)
└── docker/ #Docker with the web scraping
```

## ⚙️ Installation & Setup
* Prerequisites
* Node.js & npm
* JDK 17+
* PostgreSQL Instance

##Backend Setup
* Clone the repository.
* Update application.properties with your PostgreSQL credentials.
* Run the Spring Boot application:
``` bash
./mvnw spring-boot:run
```

## Frontend Setup
* Navigate to the frontend directory:
```bash
cd kitsune-frontend
npm i
npm run dev
```

## 🗺️ Roadmap (Future Enhancements)
- [ ] Expanded UI: Deeper React component optimization and more fluid animations.
- [ ] Offline Mode: Direct episode download feature.
- [ ] Social Integration: Ability to share watchlists with friends.
- [ ] Notification System: Alerts for new episodes of tracked shows.

## 🤝 Contributing
* Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are greatly appreciated.
