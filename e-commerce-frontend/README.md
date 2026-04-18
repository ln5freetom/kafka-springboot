# E-Commerce Angular Frontend

Angular 17 frontend for the Spring Boot E-Commerce backend.

## Features

- View all products with search
- View all users
- View all orders with status badges
- View and manage wishlist for each user

## Getting Started

### Install dependencies
```bash
npm install
```

### Start development server
```bash
npm start
```

The app will run on `http://localhost:4200/` and automatically proxy API requests to `http://localhost:8080` (the Spring Boot backend) via the proxy configuration in `src/proxy.conf.json`.

### Build for production
```bash
npm run build
```
Output will be in `dist/` directory.

## Project Structure

```
src/
├── app/
│   ├── app.component.*       # Root component
│   ├── app-routing.module.ts  # Routes
│   ├── app.module.ts          # Main module
│   ├── components/
│   │   ├── header/            # Navigation bar
│   │   ├── home/              # Home page
│   │   ├── product/           # Product list
│   │   ├── user/              # User list
│   │   ├── order/              # Order list
│   │   └── wishlist/           # Wishlist management
│   ├── models/                # TypeScript interfaces (User, Product, Order)
│   └── services/              # HTTP services connecting to backend API
├── environments/               # Environment config
└── index.html                  # Main HTML
```

## Technology

- Angular 17
- TypeScript 5.3
- Bootstrap 5 (CSS via CDN)
- RxJS
