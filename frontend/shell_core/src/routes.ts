import { createRouter, createWebHistory } from 'vue-router';
import HomePage from "./pages/HomePage.vue";

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomePage
    },
    // outras rotas...
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;