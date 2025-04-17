package com.pi.utils.interfaces;

import com.pi.utils.exceptions.GlobalException;

/**
 * The {@code IGValidationDto} interface generic defines the contract for validation and conversion operations
 * for Data Transfer Objects (DTOs) in the system.
 * <p>
 * Implementations of this interface are responsible for validating the data contained within the DTO
 * and converting it into an {@code YourClass} object. This ensures that all incoming data from external
 * sources is properly validated before being processed by the system.
 * </p>
 *
 * <p>DTOs implementing this interface should not contain any business logic. Instead, they should focus
 * on data validation and conversion to ensure the integrity and correctness of the data being processed.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * public class SomeDto implements IGValidationDto<YourClass> {
 *     private String id;
 *     private String email;
 *
 *     @Override
 *     public YourClass validate() throws GlobalException {
 *         // Perform validation logic
 *         if (id == null || !isValidUUID(id)) {
 *             throw new GlobalException("Invalid ID");
 *         }
 *         if (email == null || !isValidEmail(email)) {
 *             throw new GlobalException("Invalid email");
 *         }
 *         // Convert to YourClass object
 *         return YourClass.builder().build();
 *     }
 * }
 * }</pre>
 *
 * <p>In this example, the {@code SomeDto} class implements the {@code IGValidationDto} interface,
 * providing a concrete implementation of the {@code validate} method to ensure the data is valid
 * and converting it into an {@code YourClass} object.</p>
 * 
 * 
 *
 * @author GustavoBoaz
 * @since 1.0
 */
public interface IGValidationDto<T> {
    T validate() throws GlobalException;
}
