package fr.nekotine.fnafy.room;

public enum RoomType {
	UNKNOWN,
	VIDE,
	VISION,
	CAMERA,
	INCENDIE;
	public static RoomType fromString(String roomType) {
		for(RoomType type : RoomType.values()) {
			if(type.toString().equals(roomType)) {
				return RoomType.valueOf(type.toString());
			}
		}
		return RoomType.UNKNOWN;
	}
}
