package dev.craftport.domain;

public record ModEntry(
		String slug,
		ModSide side,
		String version) {
	public ModEntry {
		if (slug == null || slug.isBlank()) {
			throw new IllegalArgumentException("mod slug must not be blank");
		}
		if (side == null) {
			throw new IllegalArgumentException("mod side must not be null: " + slug);
		}
	}
}
