package com.pi.core_auth.core.utils.constants;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pi.core_auth.core.enums.ScopeType;
import com.pi.core_auth.core.enums.StatusType;

import java.util.EnumSet;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    public EnumSet<ScopeType> scope;
    public StatusType status;

    public Response(Builder builder) {
        this.scope = builder.scope;
        this.status = builder.status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private EnumSet<ScopeType> scope;
        private StatusType status;

        public Builder scope(EnumSet<ScopeType> scope) {
            this.scope = scope;
            return this;
        }

        public Builder status(StatusType status) {
            this.status = status;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }
}
