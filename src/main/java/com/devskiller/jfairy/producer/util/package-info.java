/**
 * Internal utility classes for the jFairy fake data producer.
 *
 * <p>This package contains stateless helper utilities used across the library:
 * <ul>
 *   <li>{@link com.devskiller.jfairy.producer.util.RandomUtils} – random string generation
 *       (numeric, alphabetic, alphanumeric)</li>
 *   <li>{@link com.devskiller.jfairy.producer.util.StringUtils} – string manipulation
 *       (padding, trimming, case conversion, accent stripping, joining, splitting, …)</li>
 *   <li>{@link com.devskiller.jfairy.producer.util.ValidateUtils} – argument validation
 *       ({@code notNull}, {@code isTrue})</li>
 *   <li>{@link com.devskiller.jfairy.producer.util.AlphaNumberSystem} – base-26 encoding
 *       used for identity-card number generation</li>
 * </ul>
 *
 * <p>All classes in this package are <strong>not part of the public API</strong> and may
 * change without notice between releases.
 */
@NullMarked
package com.devskiller.jfairy.producer.util;

import org.jspecify.annotations.NullMarked;
