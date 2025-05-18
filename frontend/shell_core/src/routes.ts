import { createRouter, createWebHistory } from 'vue-router';
import HomePage from "./pages/HomePage.vue";
import ProfilePage from "./pages/ProfilePage.vue";
import GroupsPage from "./pages/GroupsPage.vue";
import DashboardPage from "./pages/DashboardPage.vue";
import SearchPage from "./pages/SearchPage.vue";

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomePage,
        children: [
            {
                path: '/',
                name: 'home',
                component: SearchPage
            },
            {
                path: 'profile',
                name: 'profile',
                component: ProfilePage
            },
            {
                path: 'groups',
                name: 'groups',
                component: GroupsPage
            },
            {
                path: 'dashboard',
                name: 'dashboard',
                component: DashboardPage
            }
        ]
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;