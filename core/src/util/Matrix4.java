package util;

public class Matrix4 {
    private static final long serialVersionUID = -2717655254359579617L;
    /** XX: Typically the unrotated X component for scaling, also the cosine of the angle when rotated on the Y and/or Z axis. On
     * Vector3 multiplication this value is multiplied with the source X component and added to the target X component. */
    public static final int M00 = 0;
    /** XY: Typically the negative sine of the angle when rotated on the Z axis. On Vector3 multiplication this value is multiplied
     * with the source Y component and added to the target X component. */
    public static final int M01 = 4;
    /** XZ: Typically the sine of the angle when rotated on the Y axis. On Vector3 multiplication this value is multiplied with the
     * source Z component and added to the target X component. */
    public static final int M02 = 8;
    /** XW: Typically the translation of the X component. On Vector3 multiplication this value is added to the target X
     * component. */
    public static final int M03 = 12;
    /** YX: Typically the sine of the angle when rotated on the Z axis. On Vector3 multiplication this value is multiplied with the
     * source X component and added to the target Y component. */
    public static final int M10 = 1;
    /** YY: Typically the unrotated Y component for scaling, also the cosine of the angle when rotated on the X and/or Z axis. On
     * Vector3 multiplication this value is multiplied with the source Y component and added to the target Y component. */
    public static final int M11 = 5;
    /** YZ: Typically the negative sine of the angle when rotated on the X axis. On Vector3 multiplication this value is multiplied
     * with the source Z component and added to the target Y component. */
    public static final int M12 = 9;
    /** YW: Typically the translation of the Y component. On Vector3 multiplication this value is added to the target Y
     * component. */
    public static final int M13 = 13;
    /** ZX: Typically the negative sine of the angle when rotated on the Y axis. On Vector3 multiplication this value is multiplied
     * with the source X component and added to the target Z component. */
    public static final int M20 = 2;
    /** ZY: Typical the sine of the angle when rotated on the X axis. On Vector3 multiplication this value is multiplied with the
     * source Y component and added to the target Z component. */
    public static final int M21 = 6;
    /** ZZ: Typically the unrotated Z component for scaling, also the cosine of the angle when rotated on the X and/or Y axis. On
     * Vector3 multiplication this value is multiplied with the source Z component and added to the target Z component. */
    public static final int M22 = 10;
    /** ZW: Typically the translation of the Z component. On Vector3 multiplication this value is added to the target Z
     * component. */
    public static final int M23 = 14;
    /** WX: Typically the value zero. On Vector3 multiplication this value is ignored. */
    public static final int M30 = 3;
    /** WY: Typically the value zero. On Vector3 multiplication this value is ignored. */
    public static final int M31 = 7;
    /** WZ: Typically the value zero. On Vector3 multiplication this value is ignored. */
    public static final int M32 = 11;
    /** WW: Typically the value one. On Vector3 multiplication this value is ignored. */
    public static final int M33 = 15;

    static final Matrix4 tmpMat = new Matrix4();

    /** Column major order */
    public final float val[] = new float[16];

    /** Constructs an identity matrix */
    public Matrix4 () {
        val[M00] = 1f;
        val[M11] = 1f;
        val[M22] = 1f;
        val[M33] = 1f;
    }

    /** Constructs a matrix from the given matrix.
     * @param matrix The matrix to copy. (This matrix is not modified) */
    public Matrix4 (Matrix4 matrix) {
        set(matrix);
    }

    /** Constructs a matrix from the given float array. The array must have at least 16 elements; the first 16 will be copied.
     * @param values The float array to copy. Remember that this matrix is in
     *           <a href="http://en.wikipedia.org/wiki/Row-major_order">column major</a> order. (The float array is not
     *           modified) */
    public Matrix4 (float[] values) {
        set(values);
    }

    /** Sets the matrix to the given matrix.
     * @param matrix The matrix that is to be copied. (The given matrix is not modified)
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 set (Matrix4 matrix) {
        return set(matrix.val);
    }

    /** Sets the matrix to the given matrix as a float array. The float array must have at least 16 elements; the first 16 will be
     * copied.
     *
     * @param values The matrix, in float form, that is to be copied. Remember that this matrix is in
     *           <a href="http://en.wikipedia.org/wiki/Row-major_order">column major</a> order.
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 set (float[] values) {
        System.arraycopy(values, 0, val, 0, val.length);
        return this;
    }

    /** Sets the matrix to a rotation matrix representing the quaternion.
     *
     * @param quaternionX The X component of the quaternion that is to be used to set this matrix.
     * @param quaternionY The Y component of the quaternion that is to be used to set this matrix.
     * @param quaternionZ The Z component of the quaternion that is to be used to set this matrix.
     * @param quaternionW The W component of the quaternion that is to be used to set this matrix.
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 set (float quaternionX, float quaternionY, float quaternionZ, float quaternionW) {
        return set(0f, 0f, 0f, quaternionX, quaternionY, quaternionZ, quaternionW);
    }

    /** Sets the matrix to a rotation matrix representing the translation and quaternion.
     * @param translationX The X component of the translation that is to be used to set this matrix.
     * @param translationY The Y component of the translation that is to be used to set this matrix.
     * @param translationZ The Z component of the translation that is to be used to set this matrix.
     * @param quaternionX The X component of the quaternion that is to be used to set this matrix.
     * @param quaternionY The Y component of the quaternion that is to be used to set this matrix.
     * @param quaternionZ The Z component of the quaternion that is to be used to set this matrix.
     * @param quaternionW The W component of the quaternion that is to be used to set this matrix.
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 set (float translationX, float translationY, float translationZ, float quaternionX, float quaternionY,
                        float quaternionZ, float quaternionW) {
        final float xs = quaternionX * 2f, ys = quaternionY * 2f, zs = quaternionZ * 2f;
        final float wx = quaternionW * xs, wy = quaternionW * ys, wz = quaternionW * zs;
        final float xx = quaternionX * xs, xy = quaternionX * ys, xz = quaternionX * zs;
        final float yy = quaternionY * ys, yz = quaternionY * zs, zz = quaternionZ * zs;

        val[M00] = 1f - (yy + zz);
        val[M01] = xy - wz;
        val[M02] = xz + wy;
        val[M03] = translationX;

        val[M10] = xy + wz;
        val[M11] = 1f - (xx + zz);
        val[M12] = yz - wx;
        val[M13] = translationY;

        val[M20] = xz - wy;
        val[M21] = yz + wx;
        val[M22] = 1f - (xx + yy);
        val[M23] = translationZ;

        val[M30] = 0f;
        val[M31] = 0f;
        val[M32] = 0f;
        val[M33] = 1f;
        return this;
    }

    /** Sets the matrix to a rotation matrix representing the translation and quaternion.
     * @param translationX The X component of the translation that is to be used to set this matrix.
     * @param translationY The Y component of the translation that is to be used to set this matrix.
     * @param translationZ The Z component of the translation that is to be used to set this matrix.
     * @param quaternionX The X component of the quaternion that is to be used to set this matrix.
     * @param quaternionY The Y component of the quaternion that is to be used to set this matrix.
     * @param quaternionZ The Z component of the quaternion that is to be used to set this matrix.
     * @param quaternionW The W component of the quaternion that is to be used to set this matrix.
     * @param scaleX The X component of the scaling that is to be used to set this matrix.
     * @param scaleY The Y component of the scaling that is to be used to set this matrix.
     * @param scaleZ The Z component of the scaling that is to be used to set this matrix.
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 set (float translationX, float translationY, float translationZ, float quaternionX, float quaternionY,
                        float quaternionZ, float quaternionW, float scaleX, float scaleY, float scaleZ) {
        final float xs = quaternionX * 2f, ys = quaternionY * 2f, zs = quaternionZ * 2f;
        final float wx = quaternionW * xs, wy = quaternionW * ys, wz = quaternionW * zs;
        final float xx = quaternionX * xs, xy = quaternionX * ys, xz = quaternionX * zs;
        final float yy = quaternionY * ys, yz = quaternionY * zs, zz = quaternionZ * zs;

        val[M00] = scaleX * (1.0f - (yy + zz));
        val[M01] = scaleY * (xy - wz);
        val[M02] = scaleZ * (xz + wy);
        val[M03] = translationX;

        val[M10] = scaleX * (xy + wz);
        val[M11] = scaleY * (1.0f - (xx + zz));
        val[M12] = scaleZ * (yz - wx);
        val[M13] = translationY;

        val[M20] = scaleX * (xz - wy);
        val[M21] = scaleY * (yz + wx);
        val[M22] = scaleZ * (1.0f - (xx + yy));
        val[M23] = translationZ;

        val[M30] = 0f;
        val[M31] = 0f;
        val[M32] = 0f;
        val[M33] = 1f;
        return this;
    }

    /** Sets the four columns of the matrix which correspond to the x-, y- and z-axis of the vector space this matrix creates as
     * well as the 4th column representing the translation of any point that is multiplied by this matrix.
     * @param xAxis The x-axis.
     * @param yAxis The y-axis.
     * @param zAxis The z-axis.
     * @param pos The translation vector. */
    public Matrix4 set (Vector3 xAxis, Vector3 yAxis, Vector3 zAxis, Vector3 pos) {
        val[M00] = xAxis.x;
        val[M01] = xAxis.y;
        val[M02] = xAxis.z;
        val[M10] = yAxis.x;
        val[M11] = yAxis.y;
        val[M12] = yAxis.z;
        val[M20] = zAxis.x;
        val[M21] = zAxis.y;
        val[M22] = zAxis.z;
        val[M03] = pos.x;
        val[M13] = pos.y;
        val[M23] = pos.z;
        val[M30] = 0f;
        val[M31] = 0f;
        val[M32] = 0f;
        val[M33] = 1f;
        return this;
    }

    /** @return a copy of this matrix */
    public Matrix4 cpy () {
        return new Matrix4(this);
    }

    /** Adds a translational component to the matrix in the 4th column. The other columns are untouched.
     * @param vector The translation vector to add to the current matrix. (This vector is not modified)
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 trn (Vector3 vector) {
        val[M03] += vector.x;
        val[M13] += vector.y;
        val[M23] += vector.z;
        return this;
    }

    /** Adds a translational component to the matrix in the 4th column. The other columns are untouched.
     * @param x The x-component of the translation vector.
     * @param y The y-component of the translation vector.
     * @param z The z-component of the translation vector.
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 trn (float x, float y, float z) {
        val[M03] += x;
        val[M13] += y;
        val[M23] += z;
        return this;
    }

    /** @return the backing float array */
    public float[] getValues () {
        return val;
    }

    /** Postmultiplies this matrix with the given matrix, storing the result in this matrix. For example:
     *
     * <pre>
     * A.mul(B) results in A := AB.
     * </pre>
     *
     * @param matrix The other matrix to multiply by.
     * @return This matrix for the purpose of chaining operations together. */
    public Matrix4 mul (Matrix4 matrix) {
        mul(val, matrix.val);
        return this;
    }

    /** Premultiplies this matrix with the given matrix, storing the result in this matrix. For example:
     *
     * <pre>
     * A.mulLeft(B) results in A := BA.
     * </pre>
     *
     * @param matrix The other matrix to multiply by.
     * @return This matrix for the purpose of chaining operations together. */
    public Matrix4 mulLeft (Matrix4 matrix) {
        tmpMat.set(matrix);
        mul(tmpMat.val, val);
        return set(tmpMat);
    }

    /** Transposes the matrix.
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 tra () {
        float m01 = val[M01];
        float m02 = val[M02];
        float m03 = val[M03];
        float m12 = val[M12];
        float m13 = val[M13];
        float m23 = val[M23];
        val[M01] = val[M10];
        val[M02] = val[M20];
        val[M03] = val[M30];
        val[M10] = m01;
        val[M12] = val[M21];
        val[M13] = val[M31];
        val[M20] = m02;
        val[M21] = m12;
        val[M23] = val[M32];
        val[M30] = m03;
        val[M31] = m13;
        val[M32] = m23;
        return this;
    }

    /** Sets the matrix to an identity matrix.
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 idt () {
        val[M00] = 1f;
        val[M01] = 0f;
        val[M02] = 0f;
        val[M03] = 0f;
        val[M10] = 0f;
        val[M11] = 1f;
        val[M12] = 0f;
        val[M13] = 0f;
        val[M20] = 0f;
        val[M21] = 0f;
        val[M22] = 1f;
        val[M23] = 0f;
        val[M30] = 0f;
        val[M31] = 0f;
        val[M32] = 0f;
        val[M33] = 1f;
        return this;
    }

    /** Inverts the matrix. Stores the result in this matrix.
     * @return This matrix for the purpose of chaining methods together.
     * @throws RuntimeException if the matrix is singular (not invertible) */
    public Matrix4 inv () {
        float l_det = val[M30] * val[M21] * val[M12] * val[M03] - val[M20] * val[M31] * val[M12] * val[M03]
                - val[M30] * val[M11] * val[M22] * val[M03] + val[M10] * val[M31] * val[M22] * val[M03]
                + val[M20] * val[M11] * val[M32] * val[M03] - val[M10] * val[M21] * val[M32] * val[M03]
                - val[M30] * val[M21] * val[M02] * val[M13] + val[M20] * val[M31] * val[M02] * val[M13]
                + val[M30] * val[M01] * val[M22] * val[M13] - val[M00] * val[M31] * val[M22] * val[M13]
                - val[M20] * val[M01] * val[M32] * val[M13] + val[M00] * val[M21] * val[M32] * val[M13]
                + val[M30] * val[M11] * val[M02] * val[M23] - val[M10] * val[M31] * val[M02] * val[M23]
                - val[M30] * val[M01] * val[M12] * val[M23] + val[M00] * val[M31] * val[M12] * val[M23]
                + val[M10] * val[M01] * val[M32] * val[M23] - val[M00] * val[M11] * val[M32] * val[M23]
                - val[M20] * val[M11] * val[M02] * val[M33] + val[M10] * val[M21] * val[M02] * val[M33]
                + val[M20] * val[M01] * val[M12] * val[M33] - val[M00] * val[M21] * val[M12] * val[M33]
                - val[M10] * val[M01] * val[M22] * val[M33] + val[M00] * val[M11] * val[M22] * val[M33];
        if (l_det == 0f) throw new RuntimeException("non-invertible matrix");
        float m00 = val[M12] * val[M23] * val[M31] - val[M13] * val[M22] * val[M31] + val[M13] * val[M21] * val[M32]
                - val[M11] * val[M23] * val[M32] - val[M12] * val[M21] * val[M33] + val[M11] * val[M22] * val[M33];
        float m01 = val[M03] * val[M22] * val[M31] - val[M02] * val[M23] * val[M31] - val[M03] * val[M21] * val[M32]
                + val[M01] * val[M23] * val[M32] + val[M02] * val[M21] * val[M33] - val[M01] * val[M22] * val[M33];
        float m02 = val[M02] * val[M13] * val[M31] - val[M03] * val[M12] * val[M31] + val[M03] * val[M11] * val[M32]
                - val[M01] * val[M13] * val[M32] - val[M02] * val[M11] * val[M33] + val[M01] * val[M12] * val[M33];
        float m03 = val[M03] * val[M12] * val[M21] - val[M02] * val[M13] * val[M21] - val[M03] * val[M11] * val[M22]
                + val[M01] * val[M13] * val[M22] + val[M02] * val[M11] * val[M23] - val[M01] * val[M12] * val[M23];
        float m10 = val[M13] * val[M22] * val[M30] - val[M12] * val[M23] * val[M30] - val[M13] * val[M20] * val[M32]
                + val[M10] * val[M23] * val[M32] + val[M12] * val[M20] * val[M33] - val[M10] * val[M22] * val[M33];
        float m11 = val[M02] * val[M23] * val[M30] - val[M03] * val[M22] * val[M30] + val[M03] * val[M20] * val[M32]
                - val[M00] * val[M23] * val[M32] - val[M02] * val[M20] * val[M33] + val[M00] * val[M22] * val[M33];
        float m12 = val[M03] * val[M12] * val[M30] - val[M02] * val[M13] * val[M30] - val[M03] * val[M10] * val[M32]
                + val[M00] * val[M13] * val[M32] + val[M02] * val[M10] * val[M33] - val[M00] * val[M12] * val[M33];
        float m13 = val[M02] * val[M13] * val[M20] - val[M03] * val[M12] * val[M20] + val[M03] * val[M10] * val[M22]
                - val[M00] * val[M13] * val[M22] - val[M02] * val[M10] * val[M23] + val[M00] * val[M12] * val[M23];
        float m20 = val[M11] * val[M23] * val[M30] - val[M13] * val[M21] * val[M30] + val[M13] * val[M20] * val[M31]
                - val[M10] * val[M23] * val[M31] - val[M11] * val[M20] * val[M33] + val[M10] * val[M21] * val[M33];
        float m21 = val[M03] * val[M21] * val[M30] - val[M01] * val[M23] * val[M30] - val[M03] * val[M20] * val[M31]
                + val[M00] * val[M23] * val[M31] + val[M01] * val[M20] * val[M33] - val[M00] * val[M21] * val[M33];
        float m22 = val[M01] * val[M13] * val[M30] - val[M03] * val[M11] * val[M30] + val[M03] * val[M10] * val[M31]
                - val[M00] * val[M13] * val[M31] - val[M01] * val[M10] * val[M33] + val[M00] * val[M11] * val[M33];
        float m23 = val[M03] * val[M11] * val[M20] - val[M01] * val[M13] * val[M20] - val[M03] * val[M10] * val[M21]
                + val[M00] * val[M13] * val[M21] + val[M01] * val[M10] * val[M23] - val[M00] * val[M11] * val[M23];
        float m30 = val[M12] * val[M21] * val[M30] - val[M11] * val[M22] * val[M30] - val[M12] * val[M20] * val[M31]
                + val[M10] * val[M22] * val[M31] + val[M11] * val[M20] * val[M32] - val[M10] * val[M21] * val[M32];
        float m31 = val[M01] * val[M22] * val[M30] - val[M02] * val[M21] * val[M30] + val[M02] * val[M20] * val[M31]
                - val[M00] * val[M22] * val[M31] - val[M01] * val[M20] * val[M32] + val[M00] * val[M21] * val[M32];
        float m32 = val[M02] * val[M11] * val[M30] - val[M01] * val[M12] * val[M30] - val[M02] * val[M10] * val[M31]
                + val[M00] * val[M12] * val[M31] + val[M01] * val[M10] * val[M32] - val[M00] * val[M11] * val[M32];
        float m33 = val[M01] * val[M12] * val[M20] - val[M02] * val[M11] * val[M20] + val[M02] * val[M10] * val[M21]
                - val[M00] * val[M12] * val[M21] - val[M01] * val[M10] * val[M22] + val[M00] * val[M11] * val[M22];
        float inv_det = 1.0f / l_det;
        val[M00] = m00 * inv_det;
        val[M10] = m10 * inv_det;
        val[M20] = m20 * inv_det;
        val[M30] = m30 * inv_det;
        val[M01] = m01 * inv_det;
        val[M11] = m11 * inv_det;
        val[M21] = m21 * inv_det;
        val[M31] = m31 * inv_det;
        val[M02] = m02 * inv_det;
        val[M12] = m12 * inv_det;
        val[M22] = m22 * inv_det;
        val[M32] = m32 * inv_det;
        val[M03] = m03 * inv_det;
        val[M13] = m13 * inv_det;
        val[M23] = m23 * inv_det;
        val[M33] = m33 * inv_det;
        return this;
    }

    /** @return The determinant of this matrix */
    public float det () {
        return val[M30] * val[M21] * val[M12] * val[M03] - val[M20] * val[M31] * val[M12] * val[M03]
                - val[M30] * val[M11] * val[M22] * val[M03] + val[M10] * val[M31] * val[M22] * val[M03]
                + val[M20] * val[M11] * val[M32] * val[M03] - val[M10] * val[M21] * val[M32] * val[M03]
                - val[M30] * val[M21] * val[M02] * val[M13] + val[M20] * val[M31] * val[M02] * val[M13]
                + val[M30] * val[M01] * val[M22] * val[M13] - val[M00] * val[M31] * val[M22] * val[M13]
                - val[M20] * val[M01] * val[M32] * val[M13] + val[M00] * val[M21] * val[M32] * val[M13]
                + val[M30] * val[M11] * val[M02] * val[M23] - val[M10] * val[M31] * val[M02] * val[M23]
                - val[M30] * val[M01] * val[M12] * val[M23] + val[M00] * val[M31] * val[M12] * val[M23]
                + val[M10] * val[M01] * val[M32] * val[M23] - val[M00] * val[M11] * val[M32] * val[M23]
                - val[M20] * val[M11] * val[M02] * val[M33] + val[M10] * val[M21] * val[M02] * val[M33]
                + val[M20] * val[M01] * val[M12] * val[M33] - val[M00] * val[M21] * val[M12] * val[M33]
                - val[M10] * val[M01] * val[M22] * val[M33] + val[M00] * val[M11] * val[M22] * val[M33];
    }

    /** @return The determinant of the 3x3 upper left matrix */
    public float det3x3 () {
        return val[M00] * val[M11] * val[M22] + val[M01] * val[M12] * val[M20] + val[M02] * val[M10] * val[M21]
                - val[M00] * val[M12] * val[M21] - val[M01] * val[M10] * val[M22] - val[M02] * val[M11] * val[M20];
    }

    /** Sets the matrix to a projection matrix with a near- and far plane, a field of view in degrees and an aspect ratio. Note
     * that the field of view specified is the angle in degrees for the height, the field of view for the width will be calculated
     * according to the aspect ratio.
     * @param near The near plane
     * @param far The far plane
     * @param fovy The field of view of the height in degrees
     * @param aspectRatio The "width over height" aspect ratio
     * @return This matrix for the purpose of chaining methods together. */
    public static Matrix4 projection (float near, float far, float fovy, float aspectRatio) {
        float oo_farz_sub_nearz = 1.0f / (near - far);
        float oo_tan_half_fov = 1.0f / (float)Math.tan(fovy / 2);
        return new Matrix4( new float[] {
            0, -oo_tan_half_fov, 0, 0,
                    0, 0, aspectRatio*oo_tan_half_fov, 0,
                    -(near + far)*oo_farz_sub_nearz, 0, 0, 2*near*far*oo_farz_sub_nearz,
                    1, 0, 0, 0
        }).tra();
    }

    /** Sets the matrix to a projection matrix with a near/far plane, and left, bottom, right and top specifying the points on the
     * near plane that are mapped to the lower left and upper right corners of the viewport. This allows to create projection
     * matrix with off-center vanishing point.
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near The near plane
     * @param far The far plane
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 setToProjection (float left, float right, float bottom, float top, float near, float far) {
        float x = 2.0f * near / (right - left);
        float y = 2.0f * near / (top - bottom);
        float a = (right + left) / (right - left);
        float b = (top + bottom) / (top - bottom);
        float l_a1 = (far + near) / (near - far);
        float l_a2 = (2 * far * near) / (near - far);
        val[M00] = x;
        val[M10] = 0;
        val[M20] = 0;
        val[M30] = 0;
        val[M01] = 0;
        val[M11] = y;
        val[M21] = 0;
        val[M31] = 0;
        val[M02] = a;
        val[M12] = b;
        val[M22] = l_a1;
        val[M32] = -1;
        val[M03] = 0;
        val[M13] = 0;
        val[M23] = l_a2;
        val[M33] = 0;
        return this;
    }

    /** Sets this matrix to an orthographic projection matrix with the origin at (x,y) extending by width and height. The near
     * plane is set to 0, the far plane is set to 1.
     * @param x The x-coordinate of the origin
     * @param y The y-coordinate of the origin
     * @param width The width
     * @param height The height
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 setToOrtho2D (float x, float y, float width, float height) {
        setToOrtho(x, x + width, y, y + height, 0, 1);
        return this;
    }

    /** Sets this matrix to an orthographic projection matrix with the origin at (x,y) extending by width and height, having a near
     * and far plane.
     * @param x The x-coordinate of the origin
     * @param y The y-coordinate of the origin
     * @param width The width
     * @param height The height
     * @param near The near plane
     * @param far The far plane
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 setToOrtho2D (float x, float y, float width, float height, float near, float far) {
        setToOrtho(x, x + width, y, y + height, near, far);
        return this;
    }

    /** Sets the matrix to an orthographic projection like glOrtho (http://www.opengl.org/sdk/docs/man/xhtml/glOrtho.xml) following
     * the OpenGL equivalent
     * @param left The left clipping plane
     * @param right The right clipping plane
     * @param bottom The bottom clipping plane
     * @param top The top clipping plane
     * @param near The near clipping plane
     * @param far The far clipping plane
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 setToOrtho (float left, float right, float bottom, float top, float near, float far) {
        float x_orth = 2 / (right - left);
        float y_orth = 2 / (top - bottom);
        float z_orth = -2 / (far - near);

        float tx = -(right + left) / (right - left);
        float ty = -(top + bottom) / (top - bottom);
        float tz = -(far + near) / (far - near);

        val[M00] = x_orth;
        val[M10] = 0;
        val[M20] = 0;
        val[M30] = 0;
        val[M01] = 0;
        val[M11] = y_orth;
        val[M21] = 0;
        val[M31] = 0;
        val[M02] = 0;
        val[M12] = 0;
        val[M22] = z_orth;
        val[M32] = 0;
        val[M03] = tx;
        val[M13] = ty;
        val[M23] = tz;
        val[M33] = 1;
        return this;
    }

    /** Sets the 4th column to the translation vector.
     * @param vector The translation vector
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 setTranslation (Vector3 vector) {
        val[M03] = vector.x;
        val[M13] = vector.y;
        val[M23] = vector.z;
        return this;
    }

    /** Sets the 4th column to the translation vector.
     * @param x The X coordinate of the translation vector
     * @param y The Y coordinate of the translation vector
     * @param z The Z coordinate of the translation vector
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 setTranslation (float x, float y, float z) {
        val[M03] = x;
        val[M13] = y;
        val[M23] = z;
        return this;
    }

    /** Sets this matrix to a translation matrix, overwriting it first by an identity matrix and then setting the 4th column to the
     * translation vector.
     * @param vector The translation vector
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 setToTranslation (Vector3 vector) {
        idt();
        val[M03] = vector.x;
        val[M13] = vector.y;
        val[M23] = vector.z;
        return this;
    }

    /** Sets this matrix to a translation matrix, overwriting it first by an identity matrix and then setting the 4th column to the
     * translation vector.
     * @param x The x-component of the translation vector.
     * @param y The y-component of the translation vector.
     * @param z The z-component of the translation vector.
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 setToTranslation (float x, float y, float z) {
        idt();
        val[M03] = x;
        val[M13] = y;
        val[M23] = z;
        return this;
    }

    /** Sets this matrix to a translation and scaling matrix by first overwriting it with an identity and then setting the
     * translation vector in the 4th column and the scaling vector in the diagonal.
     * @param translation The translation vector
     * @param scaling The scaling vector
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 setToTranslationAndScaling (Vector3 translation, Vector3 scaling) {
        idt();
        val[M03] = translation.x;
        val[M13] = translation.y;
        val[M23] = translation.z;
        val[M00] = scaling.x;
        val[M11] = scaling.y;
        val[M22] = scaling.z;
        return this;
    }

    /** Sets this matrix to a translation and scaling matrix by first overwriting it with an identity and then setting the
     * translation vector in the 4th column and the scaling vector in the diagonal.
     * @param translationX The x-component of the translation vector
     * @param translationY The y-component of the translation vector
     * @param translationZ The z-component of the translation vector
     * @param scalingX The x-component of the scaling vector
     * @param scalingY The x-component of the scaling vector
     * @param scalingZ The x-component of the scaling vector
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 setToTranslationAndScaling (float translationX, float translationY, float translationZ, float scalingX,
                                               float scalingY, float scalingZ) {
        idt();
        val[M03] = translationX;
        val[M13] = translationY;
        val[M23] = translationZ;
        val[M00] = scalingX;
        val[M11] = scalingY;
        val[M22] = scalingZ;
        return this;
    }

    public Vector3 getTranslation (Vector3 position) {
        position.x = val[M03];
        position.y = val[M13];
        position.z = val[M23];
        return position;
    }

    /** @return the squared scale factor on the X axis */
    public float getScaleXSquared () {
        return val[M00] * val[M00] + val[M01] * val[M01] + val[M02] * val[M02];
    }

    /** @return the squared scale factor on the Y axis */
    public float getScaleYSquared () {
        return val[M10] * val[M10] + val[M11] * val[M11] + val[M12] * val[M12];
    }

    /** @return the squared scale factor on the Z axis */
    public float getScaleZSquared () {
        return val[M20] * val[M20] + val[M21] * val[M21] + val[M22] * val[M22];
    }

    /** removes the translational part and transposes the matrix. */
    public Matrix4 toNormalMatrix () {
        val[M03] = 0;
        val[M13] = 0;
        val[M23] = 0;
        return inv().tra();
    }

    public String toString () {
        return "[" + val[M00] + "|" + val[M01] + "|" + val[M02] + "|" + val[M03] + "]\n" //
                + "[" + val[M10] + "|" + val[M11] + "|" + val[M12] + "|" + val[M13] + "]\n" //
                + "[" + val[M20] + "|" + val[M21] + "|" + val[M22] + "|" + val[M23] + "]\n" //
                + "[" + val[M30] + "|" + val[M31] + "|" + val[M32] + "|" + val[M33] + "]\n";
    }

    /** Multiplies the matrix mata with matrix matb, storing the result in mata. The arrays are assumed to hold 4x4 column major
     * matrices as you can get from {@link Matrix4#val}. This is the same as {@link Matrix4#mul(Matrix4)}.
     *
     * @param mata the first matrix.
     * @param matb the second matrix. */
    public static void mul (float[] mata, float[] matb) {
        float m00 = mata[M00] * matb[M00] + mata[M01] * matb[M10] + mata[M02] * matb[M20] + mata[M03] * matb[M30];
        float m01 = mata[M00] * matb[M01] + mata[M01] * matb[M11] + mata[M02] * matb[M21] + mata[M03] * matb[M31];
        float m02 = mata[M00] * matb[M02] + mata[M01] * matb[M12] + mata[M02] * matb[M22] + mata[M03] * matb[M32];
        float m03 = mata[M00] * matb[M03] + mata[M01] * matb[M13] + mata[M02] * matb[M23] + mata[M03] * matb[M33];
        float m10 = mata[M10] * matb[M00] + mata[M11] * matb[M10] + mata[M12] * matb[M20] + mata[M13] * matb[M30];
        float m11 = mata[M10] * matb[M01] + mata[M11] * matb[M11] + mata[M12] * matb[M21] + mata[M13] * matb[M31];
        float m12 = mata[M10] * matb[M02] + mata[M11] * matb[M12] + mata[M12] * matb[M22] + mata[M13] * matb[M32];
        float m13 = mata[M10] * matb[M03] + mata[M11] * matb[M13] + mata[M12] * matb[M23] + mata[M13] * matb[M33];
        float m20 = mata[M20] * matb[M00] + mata[M21] * matb[M10] + mata[M22] * matb[M20] + mata[M23] * matb[M30];
        float m21 = mata[M20] * matb[M01] + mata[M21] * matb[M11] + mata[M22] * matb[M21] + mata[M23] * matb[M31];
        float m22 = mata[M20] * matb[M02] + mata[M21] * matb[M12] + mata[M22] * matb[M22] + mata[M23] * matb[M32];
        float m23 = mata[M20] * matb[M03] + mata[M21] * matb[M13] + mata[M22] * matb[M23] + mata[M23] * matb[M33];
        float m30 = mata[M30] * matb[M00] + mata[M31] * matb[M10] + mata[M32] * matb[M20] + mata[M33] * matb[M30];
        float m31 = mata[M30] * matb[M01] + mata[M31] * matb[M11] + mata[M32] * matb[M21] + mata[M33] * matb[M31];
        float m32 = mata[M30] * matb[M02] + mata[M31] * matb[M12] + mata[M32] * matb[M22] + mata[M33] * matb[M32];
        float m33 = mata[M30] * matb[M03] + mata[M31] * matb[M13] + mata[M32] * matb[M23] + mata[M33] * matb[M33];
        mata[M00] = m00;
        mata[M10] = m10;
        mata[M20] = m20;
        mata[M30] = m30;
        mata[M01] = m01;
        mata[M11] = m11;
        mata[M21] = m21;
        mata[M31] = m31;
        mata[M02] = m02;
        mata[M12] = m12;
        mata[M22] = m22;
        mata[M32] = m32;
        mata[M03] = m03;
        mata[M13] = m13;
        mata[M23] = m23;
        mata[M33] = m33;
    }

    /** Multiplies the vector with the given matrix. The matrix array is assumed to hold a 4x4 column major matrix as you can get
     * from {@link Matrix4#val}. The vector array is assumed to hold a 3-component vector, with x being the first element, y being
     * the second and z being the last component. The result is stored in the vector array. This is the same as
     * {@link Vector3#mul(Matrix4)}.
     * @param mat the matrix
     * @param vec the vector. */
    public static void mulVec (float[] mat, float[] vec) {
        float x = vec[0] * mat[M00] + vec[1] * mat[M01] + vec[2] * mat[M02] + mat[M03];
        float y = vec[0] * mat[M10] + vec[1] * mat[M11] + vec[2] * mat[M12] + mat[M13];
        float z = vec[0] * mat[M20] + vec[1] * mat[M21] + vec[2] * mat[M22] + mat[M23];
        vec[0] = x;
        vec[1] = y;
        vec[2] = z;
    }

    /** Multiplies the vector with the given matrix, performing a division by w. The matrix array is assumed to hold a 4x4 column
     * major matrix as you can get from {@link Matrix4#val}. The vector array is assumed to hold a 3-component vector, with x being
     * the first element, y being the second and z being the last component. The result is stored in the vector array. This is the
     * same as {@link Vector3#prj(Matrix4)}.
     * @param mat the matrix
     * @param vec the vector. */
    public static void prj (float[] mat, float[] vec) {
        float inv_w = 1.0f / (vec[0] * mat[M30] + vec[1] * mat[M31] + vec[2] * mat[M32] + mat[M33]);
        float x = (vec[0] * mat[M00] + vec[1] * mat[M01] + vec[2] * mat[M02] + mat[M03]) * inv_w;
        float y = (vec[0] * mat[M10] + vec[1] * mat[M11] + vec[2] * mat[M12] + mat[M13]) * inv_w;
        float z = (vec[0] * mat[M20] + vec[1] * mat[M21] + vec[2] * mat[M22] + mat[M23]) * inv_w;
        vec[0] = x;
        vec[1] = y;
        vec[2] = z;
    }

    /** Multiplies the vector with the top most 3x3 sub-matrix of the given matrix. The matrix array is assumed to hold a 4x4
     * column major matrix as you can get from {@link Matrix4#val}. The vector array is assumed to hold a 3-component vector, with
     * x being the first element, y being the second and z being the last component. The result is stored in the vector array. This
     * is the same as {@link Vector3#rot(Matrix4)}.
     * @param mat the matrix
     * @param vec the vector. */
    public static void rot (float[] mat, float[] vec) {
        float x = vec[0] * mat[M00] + vec[1] * mat[M01] + vec[2] * mat[M02];
        float y = vec[0] * mat[M10] + vec[1] * mat[M11] + vec[2] * mat[M12];
        float z = vec[0] * mat[M20] + vec[1] * mat[M21] + vec[2] * mat[M22];
        vec[0] = x;
        vec[1] = y;
        vec[2] = z;
    }

    /** Computes the inverse of the given matrix. The matrix array is assumed to hold a 4x4 column major matrix as you can get from
     * {@link Matrix4#val}.
     * @param values the matrix values.
     * @return false in case the inverse could not be calculated, true otherwise. */
    public static boolean inv (float[] values) {
        float l_det = det(values);
        if (l_det == 0) return false;
        float m00 = values[M12] * values[M23] * values[M31] - values[M13] * values[M22] * values[M31]
                + values[M13] * values[M21] * values[M32] - values[M11] * values[M23] * values[M32]
                - values[M12] * values[M21] * values[M33] + values[M11] * values[M22] * values[M33];
        float m01 = values[M03] * values[M22] * values[M31] - values[M02] * values[M23] * values[M31]
                - values[M03] * values[M21] * values[M32] + values[M01] * values[M23] * values[M32]
                + values[M02] * values[M21] * values[M33] - values[M01] * values[M22] * values[M33];
        float m02 = values[M02] * values[M13] * values[M31] - values[M03] * values[M12] * values[M31]
                + values[M03] * values[M11] * values[M32] - values[M01] * values[M13] * values[M32]
                - values[M02] * values[M11] * values[M33] + values[M01] * values[M12] * values[M33];
        float m03 = values[M03] * values[M12] * values[M21] - values[M02] * values[M13] * values[M21]
                - values[M03] * values[M11] * values[M22] + values[M01] * values[M13] * values[M22]
                + values[M02] * values[M11] * values[M23] - values[M01] * values[M12] * values[M23];
        float m10 = values[M13] * values[M22] * values[M30] - values[M12] * values[M23] * values[M30]
                - values[M13] * values[M20] * values[M32] + values[M10] * values[M23] * values[M32]
                + values[M12] * values[M20] * values[M33] - values[M10] * values[M22] * values[M33];
        float m11 = values[M02] * values[M23] * values[M30] - values[M03] * values[M22] * values[M30]
                + values[M03] * values[M20] * values[M32] - values[M00] * values[M23] * values[M32]
                - values[M02] * values[M20] * values[M33] + values[M00] * values[M22] * values[M33];
        float m12 = values[M03] * values[M12] * values[M30] - values[M02] * values[M13] * values[M30]
                - values[M03] * values[M10] * values[M32] + values[M00] * values[M13] * values[M32]
                + values[M02] * values[M10] * values[M33] - values[M00] * values[M12] * values[M33];
        float m13 = values[M02] * values[M13] * values[M20] - values[M03] * values[M12] * values[M20]
                + values[M03] * values[M10] * values[M22] - values[M00] * values[M13] * values[M22]
                - values[M02] * values[M10] * values[M23] + values[M00] * values[M12] * values[M23];
        float m20 = values[M11] * values[M23] * values[M30] - values[M13] * values[M21] * values[M30]
                + values[M13] * values[M20] * values[M31] - values[M10] * values[M23] * values[M31]
                - values[M11] * values[M20] * values[M33] + values[M10] * values[M21] * values[M33];
        float m21 = values[M03] * values[M21] * values[M30] - values[M01] * values[M23] * values[M30]
                - values[M03] * values[M20] * values[M31] + values[M00] * values[M23] * values[M31]
                + values[M01] * values[M20] * values[M33] - values[M00] * values[M21] * values[M33];
        float m22 = values[M01] * values[M13] * values[M30] - values[M03] * values[M11] * values[M30]
                + values[M03] * values[M10] * values[M31] - values[M00] * values[M13] * values[M31]
                - values[M01] * values[M10] * values[M33] + values[M00] * values[M11] * values[M33];
        float m23 = values[M03] * values[M11] * values[M20] - values[M01] * values[M13] * values[M20]
                - values[M03] * values[M10] * values[M21] + values[M00] * values[M13] * values[M21]
                + values[M01] * values[M10] * values[M23] - values[M00] * values[M11] * values[M23];
        float m30 = values[M12] * values[M21] * values[M30] - values[M11] * values[M22] * values[M30]
                - values[M12] * values[M20] * values[M31] + values[M10] * values[M22] * values[M31]
                + values[M11] * values[M20] * values[M32] - values[M10] * values[M21] * values[M32];
        float m31 = values[M01] * values[M22] * values[M30] - values[M02] * values[M21] * values[M30]
                + values[M02] * values[M20] * values[M31] - values[M00] * values[M22] * values[M31]
                - values[M01] * values[M20] * values[M32] + values[M00] * values[M21] * values[M32];
        float m32 = values[M02] * values[M11] * values[M30] - values[M01] * values[M12] * values[M30]
                - values[M02] * values[M10] * values[M31] + values[M00] * values[M12] * values[M31]
                + values[M01] * values[M10] * values[M32] - values[M00] * values[M11] * values[M32];
        float m33 = values[M01] * values[M12] * values[M20] - values[M02] * values[M11] * values[M20]
                + values[M02] * values[M10] * values[M21] - values[M00] * values[M12] * values[M21]
                - values[M01] * values[M10] * values[M22] + values[M00] * values[M11] * values[M22];
        float inv_det = 1.0f / l_det;
        values[M00] = m00 * inv_det;
        values[M10] = m10 * inv_det;
        values[M20] = m20 * inv_det;
        values[M30] = m30 * inv_det;
        values[M01] = m01 * inv_det;
        values[M11] = m11 * inv_det;
        values[M21] = m21 * inv_det;
        values[M31] = m31 * inv_det;
        values[M02] = m02 * inv_det;
        values[M12] = m12 * inv_det;
        values[M22] = m22 * inv_det;
        values[M32] = m32 * inv_det;
        values[M03] = m03 * inv_det;
        values[M13] = m13 * inv_det;
        values[M23] = m23 * inv_det;
        values[M33] = m33 * inv_det;
        return true;
    }

    /** Computes the determinante of the given matrix. The matrix array is assumed to hold a 4x4 column major matrix as you can get
     * from {@link Matrix4#val}.
     * @param values the matrix values.
     * @return the determinante. */
    public static float det (float[] values) {
        return values[M30] * values[M21] * values[M12] * values[M03] - values[M20] * values[M31] * values[M12] * values[M03]
                - values[M30] * values[M11] * values[M22] * values[M03] + values[M10] * values[M31] * values[M22] * values[M03]
                + values[M20] * values[M11] * values[M32] * values[M03] - values[M10] * values[M21] * values[M32] * values[M03]
                - values[M30] * values[M21] * values[M02] * values[M13] + values[M20] * values[M31] * values[M02] * values[M13]
                + values[M30] * values[M01] * values[M22] * values[M13] - values[M00] * values[M31] * values[M22] * values[M13]
                - values[M20] * values[M01] * values[M32] * values[M13] + values[M00] * values[M21] * values[M32] * values[M13]
                + values[M30] * values[M11] * values[M02] * values[M23] - values[M10] * values[M31] * values[M02] * values[M23]
                - values[M30] * values[M01] * values[M12] * values[M23] + values[M00] * values[M31] * values[M12] * values[M23]
                + values[M10] * values[M01] * values[M32] * values[M23] - values[M00] * values[M11] * values[M32] * values[M23]
                - values[M20] * values[M11] * values[M02] * values[M33] + values[M10] * values[M21] * values[M02] * values[M33]
                + values[M20] * values[M01] * values[M12] * values[M33] - values[M00] * values[M21] * values[M12] * values[M33]
                - values[M10] * values[M01] * values[M22] * values[M33] + values[M00] * values[M11] * values[M22] * values[M33];

    }

    /** Postmultiplies this matrix by a translation matrix. Postmultiplication is also used by OpenGL ES'
     * glTranslate/glRotate/glScale
     * @param translation
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 translate (Vector3 translation) {
        return translate(translation.x, translation.y, translation.z);
    }

    /** Postmultiplies this matrix by a translation matrix. Postmultiplication is also used by OpenGL ES' 1.x
     * glTranslate/glRotate/glScale.
     * @param x Translation in the x-axis.
     * @param y Translation in the y-axis.
     * @param z Translation in the z-axis.
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 translate (float x, float y, float z) {
        val[M03] += val[M00] * x + val[M01] * y + val[M02] * z;
        val[M13] += val[M10] * x + val[M11] * y + val[M12] * z;
        val[M23] += val[M20] * x + val[M21] * y + val[M22] * z;
        val[M33] += val[M30] * x + val[M31] * y + val[M32] * z;
        return this;
    }

    /** Postmultiplies this matrix with a scale matrix. Postmultiplication is also used by OpenGL ES' 1.x
     * glTranslate/glRotate/glScale.
     * @param scaleX The scale in the x-axis.
     * @param scaleY The scale in the y-axis.
     * @param scaleZ The scale in the z-axis.
     * @return This matrix for the purpose of chaining methods together. */
    public Matrix4 scale (float scaleX, float scaleY, float scaleZ) {
        val[M00] *= scaleX;
        val[M01] *= scaleY;
        val[M02] *= scaleZ;
        val[M10] *= scaleX;
        val[M11] *= scaleY;
        val[M12] *= scaleZ;
        val[M20] *= scaleX;
        val[M21] *= scaleY;
        val[M22] *= scaleZ;
        val[M30] *= scaleX;
        val[M31] *= scaleY;
        val[M32] *= scaleZ;
        return this;
    }

    /** Copies the 4x3 upper-left sub-matrix into float array. The destination array is supposed to be a column major matrix.
     * @param dst the destination matrix */
    public void extract4x3Matrix (float[] dst) {
        dst[0] = val[M00];
        dst[1] = val[M10];
        dst[2] = val[M20];
        dst[3] = val[M01];
        dst[4] = val[M11];
        dst[5] = val[M21];
        dst[6] = val[M02];
        dst[7] = val[M12];
        dst[8] = val[M22];
        dst[9] = val[M03];
        dst[10] = val[M13];
        dst[11] = val[M23];
    }

    public static Matrix4 rotate_xyz(float rotX, float rotY, float rotZ)
    {

        float sX = (float)Math.sin(rotX);
        float cX = (float)Math.cos(rotX);
        float sY = (float)Math.sin(rotY);
        float cY = (float)Math.cos(rotY);
        float sZ = (float)Math.sin(rotZ);
        float cZ = (float)Math.cos(rotZ);

        return new Matrix4(new float[]{
                (cY * cZ), (-sX * sY * cZ - cX * sZ), (-cX * sY * cZ + sX * sZ), 0,
                (cY * sZ), (-sX * sY * sZ + cX * cZ), (-cX * sY * sZ - sX * cZ), 0,
                (sY), (sX * cY), (cX * cY), 0,
                0, 0, 0, 1}).tra();
    }
}
