package org.firefly.provider.cryptography.rsa;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KeyPair {
    private byte[] publicKey;
    private byte[] privateKey;
}
