package com.pi.core_auth.core.utils.constants;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pi.core_auth.core.enums.ScopeType;
import com.pi.core_auth.core.enums.StatusType;

import java.util.EnumSet;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    /**
     * EnumSet representing the scope of the response.
     */
    public EnumSet<ScopeType> scope;

    /**
     * Enum representing the status of the response.
     */
    public StatusType status;

    /**
     * Constructor for the Response class.
     *
     * @param builder The builder instance used to create the response.
     */
    private Response(Builder builder) {
        this.scope = builder.scope;
        this.status = builder.status;
    }

    /**
     * Static method to create a new Builder instance.
     *
     * @return A new Builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Static class to create a new Response instance using the Builder pattern.
     */
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
