package co.simplon.flashback.dtos;

import java.util.Collection;

public class FavoritesAndLabels {

    private Collection<MovieForSearch> favorites;

    private Collection<DeviceDetails> devices;

    public FavoritesAndLabels() {
    }

    public Collection<MovieForSearch> getFavorites() {
	return favorites;
    }

    public void setFavorites(
	    Collection<MovieForSearch> favorites) {
	this.favorites = favorites;
    }

    public Collection<DeviceDetails> getDevices() {
	return devices;
    }

    public void setDevices(
	    Collection<DeviceDetails> devices) {
	this.devices = devices;
    }

    @Override
    public String toString() {
	return " {favorites=" + favorites + ", devices="
		+ devices + "}";
    }
}
