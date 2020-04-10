package seedu.address.commons.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Arrays;

/**
 * An algorithm for measuring the difference between two character sequences.
 *
 * <p>This is the number of changes needed to change one sequence into another, where each change is
 * a single character modification (deletion, insertion or substitution).
 *
 * <p>This code has been adapted from Apache Commons Lang 3.3.
 *
 * @since 1.0
 */
// @@author BransonNg-reused
public class LevenshteinDistance {

    /**
     * Find the Levenshtein distance between two CharSequences if it's less than or equal to a given
     * threshold.
     *
     * <p>This implementation follows from Algorithms on Strings, Trees and Sequences by Dan
     * Gusfield and Chas Emerick's implementation of the Levenshtein distance algorithm from <a
     * href="http://www.merriampark.com/ld.htm" >http://www.merriampark.com/ld.htm</a>
     *
     * <pre>
     * limitedCompare(null, *, *)             = IllegalArgumentException
     * limitedCompare(*, null, *)             = IllegalArgumentException
     * limitedCompare(*, *, -1)               = IllegalArgumentException
     * limitedCompare("","", 0)               = 0
     * limitedCompare("aaapppp", "", 8)       = 7
     * limitedCompare("aaapppp", "", 7)       = 7
     * limitedCompare("aaapppp", "", 6))      = -1
     * limitedCompare("elephant", "hippo", 7) = 7
     * limitedCompare("elephant", "hippo", 6) = -1
     * limitedCompare("hippo", "elephant", 7) = 7
     * limitedCompare("hippo", "elephant", 6) = -1
     * </pre>
     *
     * @param left the first string, must not be null
     * @param right the second string, must not be null
     * @param threshold the target threshold, must not be negative
     * @return result distance, or -1
     */
    // @@auther BransonNg-reused
    public static int levenshteinDistanceCompare(
            CharSequence left, CharSequence right, final int threshold) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
        }

        int leftLength = left.length(); // length of left
        int rightLength = right.length(); // length of right

        // if one string is empty, the edit distance is necessarily the length
        // of the other
        if (leftLength == 0) {
            return rightLength <= threshold ? rightLength : -1;
        } else if (rightLength == 0) {
            return leftLength <= threshold ? leftLength : -1;
        }

        if (leftLength > rightLength) {
            // swap the two strings to consume less memory
            final CharSequence tmp = left;
            left = right;
            right = tmp;
            leftLength = rightLength;
            rightLength = tmp.length();
        }

        int[] p = new int[leftLength + 1]; // 'previous' cost array, horizontally
        int[] d = new int[leftLength + 1]; // cost array, horizontally
        int[] tempD; // placeholder to assist in swapping p and d

        // fill in starting table values
        final int boundary = Math.min(leftLength, threshold) + 1;
        for (int i = 0; i < boundary; i++) {
            p[i] = i;
        }
        // these fills ensure that the value above the rightmost entry of our
        // stripe will be ignored in following loop iterations
        Arrays.fill(p, boundary, p.length, Integer.MAX_VALUE);
        Arrays.fill(d, Integer.MAX_VALUE);

        // iterates through t
        for (int j = 1; j <= rightLength; j++) {
            final char rightJ = right.charAt(j - 1); // jth character of right
            d[0] = j;

            // compute stripe indices, constrain to array size
            final int min = Math.max(1, j - threshold);
            final int max =
                    j > Integer.MAX_VALUE - threshold
                            ? leftLength
                            : Math.min(leftLength, j + threshold);

            // the stripe may lead off of the table if s and t are of different
            // sizes
            if (min > max) {
                return -1;
            }

            // ignore entry left of leftmost
            if (min > 1) {
                d[min - 1] = Integer.MAX_VALUE;
            }

            // iterates through [min, max] in s
            for (int i = min; i <= max; i++) {
                if (left.charAt(i - 1) == rightJ) {
                    // diagonally left and up
                    d[i] = p[i - 1];
                } else {
                    // 1 + minimum of cell to the left, to the top, diagonally
                    // left and up
                    d[i] = 1 + Math.min(Math.min(d[i - 1], p[i]), p[i - 1]);
                }
            }

            // copy current distance counts to 'previous row' distance counts
            tempD = p;
            p = d;
            d = tempD;
        }

        // if p[n] is greater than the threshold, there's no guarantee on it
        // being the correct
        // distance
        if (p[leftLength] <= threshold) {
            return p[leftLength];
        }
        return -1;
    }
}
