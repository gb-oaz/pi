import { defineStore } from 'pinia'
import { AuthApi } from '../services/auth/AuthApi.ts'
import type { IScope } from "../services/auth/types/IScope.ts"
import { computed, ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
    const authApi = new AuthApi()
    const scope = ref<IScope | null>(authApi.getScopeLocalStorage())

    // Computed properties
    const isAuthenticated = computed(() => {
        return scope.value && !scope.value.scope.includes('ANONYMOUS')
    })

    const isAnonymous = computed(() => {
        return scope.value && scope.value.scope.includes('ANONYMOUS')
    })

    const userRole = computed(() => {
        return scope.value?.scope || ['ANONYMOUS']
    })

    const hasAnyRole = (requiredRoles: string[]) => {
        if (!scope.value) return false
        return requiredRoles.some(role => scope.value?.scope.includes(role))
    }

    // Actions
    function updateScope() {
        scope.value = authApi.getScopeLocalStorage()
    }

    // Initialize if scope is null
    if (!scope.value) {
        updateScope()
    }

    return {
        scope,
        isAuthenticated,
        isAnonymous,
        userRole,
        updateScope,
        hasAnyRole
    }
})