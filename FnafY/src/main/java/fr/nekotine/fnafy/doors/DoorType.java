package fr.nekotine.fnafy.doors;

public enum DoorType {
	Unknown,
	Classique,
	CoupeFeux,
	Pneumatique;
	public static DoorType fromString(String doorType) {
		for(DoorType type : DoorType.values()) {
			if(type.toString().equals(doorType)) {
				return DoorType.valueOf(type.toString());
			}
		}
		return DoorType.Unknown;
	}
}
