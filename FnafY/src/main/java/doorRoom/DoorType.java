package doorRoom;

public enum DoorType {
	UNKNOWN,
	CLASSIQUE,
	COUPEFEUX,
	PNEUMATIQUE;
	public static DoorType fromString(String doorType) {
		for(DoorType type : DoorType.values()) {
			if(type.toString().equals(doorType)) {
				return DoorType.valueOf(type.toString());
			}
		}
		return DoorType.UNKNOWN;
	}
}
