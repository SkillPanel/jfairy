package com.devskiller.jfairy.producer.company;

public final class CompanyProperties {

	private CompanyProperties() {
	}

	public abstract static class CompanyProperty {

		public abstract void apply(CompanyProvider company);

		public static CompanyProperty withName(final String name) {
			return new CompanyProperty() {
				@Override
				public void apply(CompanyProvider companyProvider) {
					companyProvider.setName(name);
				}
			};
		}

		public static CompanyProperty withDomain(final String domain) {
			return new CompanyProperty() {
				@Override
				public void apply(CompanyProvider companyProvider) {
					companyProvider.setDomain(domain);
				}
			};
		}

		public static CompanyProperty withEmail(final String email) {
			return new CompanyProperty() {
				@Override
				public void apply(CompanyProvider companyProvider) {
					companyProvider.setEmail(email);
				}
			};
		}

		public static CompanyProperty withVatIdentificationNumber(final String vatIdentificationNumber) {
			return new CompanyProperty() {
				@Override
				public void apply(CompanyProvider companyProvider) {
					companyProvider.setVatIdentificationNumber(vatIdentificationNumber);
				}
			};
		}

	}
}
