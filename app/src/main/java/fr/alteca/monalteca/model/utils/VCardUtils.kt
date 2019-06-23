package fr.alteca.monalteca.model.utils


import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import fr.alteca.monalteca.model.VCard
import fr.alteca.monalteca.textrazor.model.Entity

class VCardUtils {

    companion object {

        /***
         * Transforms the FirebaseVisionBarcode.ContactInfo into a VCard custom Object
         * @param contactsInfo ContactInfo from Barcode
         * @return VCard
         */
        fun vCardFromContactInfo(contactsInfo: FirebaseVisionBarcode.ContactInfo): VCard {
            val vCard = VCard()
            vCard.fullName= contactsInfo.name?.first ?: "" + " " +contactsInfo.name?.last ?: ""
            vCard.org = contactsInfo.organization ?: ""
            vCard.title = contactsInfo.title ?: ""
            vCard.tels = Array(contactsInfo.phones.size)
            {
                VCard.Tel(telephoneType(contactsInfo.phones[it].type), contactsInfo.phones[it].number ?: "")
            }
            vCard.emails = Array(contactsInfo.emails.size) {
                VCard.Email(mailType(contactsInfo.emails[it].type), contactsInfo.emails[it].address ?: "")
            }
            vCard.adrs= Array(contactsInfo.addresses.size){
                VCard.Adr(
                    addressType(contactsInfo.addresses[it].type),
                    contactsInfo.addresses[it].addressLines.toString()
                )
            }
            return vCard
        }

        fun createStringVcard(vCard: VCard):String{
            return  "BEGIN:VCARD\n" +
                    "VERSION:2.1\n" +
                    "N:"+ vCard.fullName+ "\n" +
                    "FN:"+vCard.fullName+"\n" +
                    "ORG:Alteca\n" +
                    "TITLE:"+vCard.title+"\n" +
                    "TEL;WORK;VOICE:"+ vCard.tels[0].Telnumber +"\n" +
                    "TEL;HOME;VOICE:"+vCard.tels[1].Telnumber+"\n" +
                    "ADR;WORK;PREF:"+vCard.adrs[0].adr+"\n" +
                    "EMAIL:"+vCard.emails[0].email+"\n" +
                    "END:VCARD"
        }

        /**
         * Transform the  FirebaseVisionBarcode.Address into EntryType
         * @param type FirebaseVisionBarcode.Address.TYPE_XXX
         * @return EntryType
         */
        private fun addressType(type: Int):String{
            return when(type){
                FirebaseVisionBarcode.Address.TYPE_HOME->EntryType.Home.name
                FirebaseVisionBarcode.Address.TYPE_WORK-> EntryType.Work.name
                else-> EntryType.Unknown.name
            }
        }

        /**
         * Transform the  FirebaseVisionBarcode.Address into EntryType
         * @param type FirebaseVisionBarcode.Phone.TYPE_XXX
         * @return EntryType
         */
        private fun telephoneType(type: Int): String {
            return when (type) {
                FirebaseVisionBarcode.Phone.TYPE_MOBILE -> EntryType.Mobile.name
                FirebaseVisionBarcode.Phone.TYPE_HOME -> EntryType.Home.name
                FirebaseVisionBarcode.Phone.TYPE_WORK -> EntryType.Work.name
                FirebaseVisionBarcode.Phone.TYPE_FAX -> EntryType.Fax.name
                else -> EntryType.Unknown.name
            }

        }

        /**
         * Transform the  FirebaseVisionBarcode.Address into EntryType
         * @param type FirebaseVisionBarcode.MAIL.TYPE_XXX
         * @return EntryType
         */
        private fun mailType(type: Int): String {
            return when(type){
                FirebaseVisionBarcode.Email.TYPE_HOME-> EntryType.Home.name
                FirebaseVisionBarcode.Email.TYPE_WORK-> EntryType.Work.name
                else-> EntryType.Unknown.name
            }
        }

        /**Affects the value depending ontthe NER Entity type
         * @param vCard target Vcard
         * @param entity TextRazor entity
         * */
        fun setVCardProperty(vCard: VCard,entity: Entity){
            for (entityType in entity.type){
                when(entityType){
                    NEREntityType.Company.name -> vCard.org=entity.entityId
                    NEREntityType.Person.name -> vCard.fullName=entity.entityId
                    NEREntityType.Email.name -> vCard.emails.plus(VCard.Email(EntryType.Unknown.name,entity.entityId))
                }
            }
        }

        enum class EntryType{
            Home,
            Work,
            Mobile,
            Fax,
            Unknown
        }

        enum class NEREntityType{
            Company,
            Person,
            Email
        }
    }
}