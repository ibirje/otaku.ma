package ma.otaku.business.stats;

public class ResumeCommandes {

        private Long pendingAcceptation;
        private Long pendingPreparation;
        private Long pendingEnvoi;
        private Long pendingLivraison;
        
        public ResumeCommandes() 
        {
			pendingAcceptation = 0l;
			pendingPreparation = 0l;
			pendingEnvoi 	   = 0l;
			pendingLivraison   = 0l;
		}
        
		public Long getPendingAcceptation() {
			return pendingAcceptation;
		}
		public void setPendingAcceptation(Long pendingAcceptation) {
			this.pendingAcceptation = pendingAcceptation;
		}
		public Long getPendingPreparation() {
			return pendingPreparation;
		}
		public void setPendingPreparation(Long pendingPreparation) {
			this.pendingPreparation = pendingPreparation;
		}
		public Long getPendingEnvoi() {
			return pendingEnvoi;
		}
		public void setPendingEnvoi(Long pendingEnvoi) {
			this.pendingEnvoi = pendingEnvoi;
		}
		public Long getPendingLivraison() {
			return pendingLivraison;
		}
		public void setPendingLivraison(Long pendingLivraison) {
			this.pendingLivraison = pendingLivraison;
		}
}
