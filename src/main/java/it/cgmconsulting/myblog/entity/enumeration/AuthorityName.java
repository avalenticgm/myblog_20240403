package it.cgmconsulting.myblog.entity.enumeration;

public enum AuthorityName {

    ADMIN,
    WRITER, // scrive post
    MEMBER, // scrive commenti e vota i post
    MODERATOR, // si occupa delle segnalazioni
    GUEST // stato in cui versa un utente che si è registrato ma non ha ancora confermato la propria email
}
