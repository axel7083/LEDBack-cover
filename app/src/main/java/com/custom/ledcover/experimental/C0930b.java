package com.custom.ledcover.experimental;

public class C0930b {

    /* renamed from: a */
    static final /* synthetic */ boolean f839a = (!C0930b.class.desiredAssertionStatus());

    /* renamed from: b */
    private String readLine = null;

    /* renamed from: c */
    private int f841c = 0;

    /* renamed from: d */
    private int f842d = 0;

    /* renamed from: e */
    private int f843e = 0;

    /* renamed from: f */
    private int f844f = 0;

    /* renamed from: g */
    private byte[] f845g = null;

    public C0930b(String str) {
        this.readLine = str;
        if (!isValid()) {
            throw new IllegalArgumentException(str);
        }
    }

    /* renamed from: a */
    private byte[] m1009a(int i) {
        byte[] bArr = new byte[i];
        for (int i2 = 0; i2 != i; i2++) {
            int i3 = (i2 * 2) + 9;
            bArr[i2] = (byte) Integer.parseInt(this.readLine.substring(i3, i3 + 2), 16);
        }
        if (f839a || bArr.length == i) {
            return bArr;
        }
        throw new AssertionError();
    }

    /* renamed from: e */
    private char m1010e() {
        return this.readLine.charAt(0);
    }

    /* renamed from: f */
    private int getDecimalLength() {
        return Integer.parseInt(this.readLine.substring(1, 3), 16);
    }

    /* renamed from: g */
    private int getDecimalAddress() {
        return Integer.parseInt(this.readLine.substring(3, 7), 16);
    }

    /* renamed from: h */
    private int m1013h() {
        return Integer.parseInt(this.readLine.substring(7, 9), 16);
    }

    /* renamed from: i */
    private int getCheckSum() {
        return Integer.parseInt(this.readLine.substring(this.readLine.length() - 2), 16);
    }

    /* renamed from: j */
    private int computeCheckSum() {
        int i = 0;
        int length = this.readLine.length();
        for (int i2 = 1; i2 != length - 2; i2 += 2) {
            i += Integer.parseInt(this.readLine.substring(i2, i2 + 2), 16);
        }
        return (256 - (i % 256)) % 256;
    }

    /* renamed from: k */
    private boolean isValid() {
        if (m1010e() == ':') {
            this.f841c = getDecimalLength();
            this.f842d = getDecimalAddress();
            this.f843e = m1013h();
            this.f845g = m1009a(this.f841c);
            this.f844f = getCheckSum();
            System.out.println("address: " + Integer.toHexString(this.f842d));
            System.out.println("length: " + Integer.toHexString(this.f841c));
            System.out.println("checksum: " + Integer.toString(this.f844f));
            for (int i = 0; i < this.f841c; i++) {
                System.out.print(String.format("%02X ", Byte.valueOf(this.f845g[i])));
            }
            System.out.println();
            System.out.println("< Done >");
            if (this.f844f == computeCheckSum()) {
                return true;
            }
        }
        return false;
    }

    /* renamed from: a */
    public String mo9161a() {
        return Integer.toHexString(this.f841c);
    }

    /* renamed from: b */
    public int mo9162b() {
        return this.f842d;
    }

    /* renamed from: c */
    public String mo9163c() {
        return this.readLine.substring(3, 7);
    }

    /* renamed from: d */
    public byte[] mo9164d() {
        return this.f845g;
    }
}
