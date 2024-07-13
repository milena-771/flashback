package co.simplon.flashback.dtos;

import java.util.Collection;

public class FavoriteAndLabelsAndRetroDetailsForUpdate {

    private Collection<MovieForSearch> favorites;

    private Collection<DeviceDetails> devices;

    private OrgaRetroForUpdate retroDetails;

    public FavoriteAndLabelsAndRetroDetailsForUpdate() {
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

    public OrgaRetroForUpdate getRetroDetails() {
	return retroDetails;
    }

    public void setRetroDetails(
	    OrgaRetroForUpdate retroDetails) {
	this.retroDetails = retroDetails;
    }

    @Override
    public String toString() {
	return " {favorites=" + favorites + ", devices="
		+ devices + ", retroDetails=" + retroDetails
		+ "}";
    }
}
