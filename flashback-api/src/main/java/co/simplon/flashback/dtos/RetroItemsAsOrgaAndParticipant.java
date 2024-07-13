package co.simplon.flashback.dtos;

import java.util.Collection;

public class RetroItemsAsOrgaAndParticipant {

    private Collection<RetroItem> retroByOrga;

    private Collection<RetroItem> retroByParticipant;

    public RetroItemsAsOrgaAndParticipant() {
    }

    public Collection<RetroItem> getRetroByOrga() {
	return retroByOrga;
    }

    public void setRetroByOrga(
	    Collection<RetroItem> retroByOrga) {
	this.retroByOrga = retroByOrga;
    }

    public Collection<RetroItem> getRetroByParticipant() {
	return retroByParticipant;
    }

    public void setRetroByParticipant(
	    Collection<RetroItem> retroByParticipant) {
	this.retroByParticipant = retroByParticipant;
    }

    @Override
    public String toString() {
	return " {retroByOrga=" + retroByOrga
		+ ", retroByParticipant="
		+ retroByParticipant + "}";
    }
}
