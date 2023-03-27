package de.garrafao.phitag.domain.helper;

import lombok.Getter;

@Getter
public class Pair<L, R> {
    
    private final L left;
    private final R right;

    public Pair(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> Pair<L, R> of(final L left, final R right) {
        return new Pair<>(left, right);
    }
    
}
