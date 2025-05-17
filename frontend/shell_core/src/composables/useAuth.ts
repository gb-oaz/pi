import { ref, computed } from 'vue'
import { AuthApi } from '../services/auth/AuthApi.ts'
import type { IScope } from "../services/auth/types/IScope.ts"

export function useAuth() {
    const authApi = new AuthApi()
    const scope = ref<IScope | null>(authApi.getScopeLocalStorage())

    const isAuthenticated = computed(() => {
        return scope.value && !scope.value.scope.includes('ANONYMOUS')
    })

    const isAnonymous = computed(() => {
        return !scope.value || scope.value.scope.includes('ANONYMOUS')
    })

    const userRole = computed(() => {
        return scope.value?.scope || 'ANONYMOUS'
    })

    function updateScope() {
        scope.value = authApi.getScopeLocalStorage()
    }

    return {
        isAuthenticated,
        isAnonymous,
        userRole,
        updateScope
    }
}