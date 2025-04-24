package com.pi.core_quiz.core.utils.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    /**
     * String representing the key of the response.
     */
    public String key;

    private Response(Builder builder) {
        this.key = builder.key;
    }

    /**
     * Static method to create a new Builder instance.
     *
     * @return A new Builder instance.
     */
    public static Builder builder() { return new Builder(); }

    /**
     * Static class to create a new Response instance using the Builder pattern.
     */
    public static class Builder {
        private String key;

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }
}
