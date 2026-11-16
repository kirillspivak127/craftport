package dev.craftport.domain;

public record HostDefinition(
		String id,
		String name,
		String address,
		int maxMemoryGb,
		HostDriverType driver) {
	public HostDefinition {
		if (id == null || id.isBlank()) {
			throw new IllegalArgumentException("host id must not be blank");
		}
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("host name must not be blank");
		}
		if (address == null || address.isBlank()) {
			throw new IllegalArgumentException("host address must not be blank: " + id);
		}
		if (maxMemoryGb <= 0) {
			throw new IllegalArgumentException("host maxMemoryGb must be positive: " + maxMemoryGb);
		}
		if (driver == null) {
			throw new IllegalArgumentException("host driver must not be null: " + id);
		}
	}
}
