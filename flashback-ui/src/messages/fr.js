const messages = {
    appTitle:'Flashback app',
    menu:{
        flash:"flash",
        back:"back",
        admin:"Accueil",
        createMovie:"Ajouter un film", 
        logOut:"Déconnexion",
        welcome:"Bienvenue",
        signIn:"Connexion",
        signUp:"Inscription",
        films:"Films",
        organize:"Organiser",
        planning:"Planning",
        retroToCome:"A venir",
        retroAdmin:"Rétrospectives",
        favorites:"Favoris",
        retrospectives:"Rétrospectives"
    },
    titles : {
        organizeRetro : "Nouvelle rétrospective",
        createMovie: "Nouvelle fiche film",
        editAll:"Editer",
        results:"Résultats",
        allFilms:"Catalogue",
        updateMovie:"Mettre à jour une fiche film",
        signUp:"Créer un compte",
        search:"Films",
        notFound:"Not Found",
        forbidden:"Forbidden",
        internalError:"Internal Error",
        unauthorized:"Unauthorized",
        favorites:"Mes films",
        planning:"Mon Planning",
        organization:"Organisation",
        participation:"Participation",
        retroToCome:"Rétrospectives à venir",
        retroUpdate:"Modifier la rétrospective",
        retroAdmin:"Editer les rétrospectives",
        userAdmin:"Utilisateurs"
    },
    form:{
        nameLabel: "Nom",
        startDate: "Date de début",
        endDate: "Date de fin",
        viewingMode: "Où voir les films ?",
        image: "Image",
        description: "Description",
        placeholder:{
            searchDirector:"Veuillez rechercher par nom de famille."
        },
        helpText:{
            pattern:"Caractères spéciaux non-autorisés.",
            isan:{
                default:"33 caractères max.",
                pattern:"ISAN invalide.",
                recorded:"Ne peut pas être modifié."
            },
            title:{
                default:"100 caractères max."
            },
            releaseYear:{
                between:"Doit être comprise entre 1895 et 2025 inclus.",
                integer:"Doit être un nombre entier."
            },
            select:"Veuillez choisir parmi la liste:",
            directors:"Sélectionnez 1 à 3 cinéastes max.",
            directorsRecorded:"Cinéaste(s) enregistré(s) : ",
            genreRecorded:"Genre enregistré : ",
            poster:"La taille de l'image ne doit pas dépasser 200ko.",
            trailer:{
                url:"Ce lien ne correspond pas à une url.",
                default:"300 caractères max."
            },
            summary:{
                default:"1000 caractères max."
            },
            search:"Veuillez indiquer un titre de film, un nom de cinéaste ou un genre.",
            searchByTitle:"Veuillez indiquer un titre de film.",
            firstname:{
                default:"50 caractères max."
            },
            email:{
                format:"Cette adresse ne correspond pas au format email."
            },
            password:{
                default:"ex: Lumiere1895!",
                pattern :"Doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial (%!*)"
            },
            signIn:{
                failed:"Identifiants invalides."
            },
            retro:{
                name:"150 caractères max.",
                device:"Veuillez sélectionner un mode de visionnage.",
                endDate:"La date de fin doit être postérieure à la date de début."
            }
        }
    },
    tooltip:{
        messages:{
            uniqueIsan:"Cet ISAN existe déjà.",
            uniqueTrailer:"L'url de cette bande-annonce existe déjà.",
            uniqueEmail:"Un compte existe déjà avec cet email.",
            uniqueRetroName:"Une rétrospective existe déjà avec ce nom."
        }
    },
    toast:{
        movie:{
            create:"La fiche film a été créée avec succès.",
            update:"La fiche film a été modifiée avec succès.",
            invalid:"La fiche film est invalide.",
            delete:"La fiche film de {film} a été supprimée avec succès.",
            errorDelete:"Une erreur s'est produite, la fiche film n'a pas pu être supprimée.",
            errorCreate:"La fiche film n'a pas pu être enregistrée."
        },
        deleteFavorite:{
            success:"{film} a été supprimé de vos favoris.",
            error:"Une erreur s'est produite, le film n'a pas pu être supprimé de vos favoris."
        },
        signIn:{
            error:"Une erreur s'est produite, la connexion n'a pas fonctionnée."
        },
        signUp:{
            success:"Votre compte a été créé avec succès, vous pouvez accéder à votre profile."
        },
        retro:{
            create:"La rétrospective a été créée avec succès.",
            invalid:"La rétrospective est invalide.",
            delete:"Vous avez supprimé la rétrospective: {retro}.",
            update:"La rétrospective a été modifiée avec succès.",
            errorCreate:"Une erreur s'est produite, votre rétrospective n'a pas pu être enregistrée.",
            errorUpdate:"Une erreur s'est produite, votre rétrospective n'a pas pu être modifiée.",
            errorDelete:"Une erreur s'est produite, la rétrospective n'a pas pu être supprimée."
        },
        participate:{
            success:"Vous participez à la rétrospective: {retro}",
            error:"Une erreur s'est produite, la participation à la rétrospective n'a pas pu être enregistrée."
        },
        cancelParticipation:{
            success:"Vous ne participez plus à la rétrospective: {retro}",
            error:"Une erreur s'est produite, l'annulation n'a pas pu être enregistrée."
        },
        user:{
            delete:"Vous avez supprimé l'utilisateur: {email}.",
            errorDelete:"Une erreur s'est produite, l'utilisateur n'a pas pu être supprimé."
        }

    },
    label:{
        isan:"ISAN",
        title:"Titre",
        releaseYear:"Année de sortie",
        genre:"Genre",
        directors:"Cinéaste(s)",
        director: "Cinéaste",
        poster:"Affiche du film",
        trailer:"Bande-annonce",
        summary:"Synopsis",
        posterEdit:"Affiche",
        update:"Modifier",
        delete:"Supprimer",
        searchMovie:"Rechercher des films",
        firstname:"Prénom",
        lastname:"Nom",
        email:"Email",
        password:"Mot de passe",
        noResult:"Aucun film ne correspond à ces critères.",
        searchQuestion:"Quel film souhaitez-vous (re)voir ?",
        directedBy:"Réalisé par",
        noFavorite:"Vous n'avez pas de films en favoris.",
        retroName:"Nom",
        devices:"Où voir les films ?",
        startDate:"Date de début",
        endDate:"Date de fin",
        description:"Description",
        movies:"Films à (re)voir",
        noMovieSelected:"Vous n'avez pas sélectionné de films.",
        noParticipation:"Vous ne participez à aucune rétrospective.",
        noOrganization:"Vous n'avez pas organisé de rétrospectives.",
        noRetroToCome:"Aucune rétrospective à venir pour le moment.",
        organizer:"Organisé par",
        details:"Détails",
        noRetroAdmin:"Aucune rétrospective n'est planifiée actuellement.",
        registration:"Inscription",
        filterBy:"Filtrer par"

    },
    button:{
        create:"Ajouter",
        search:"Rechercher",
        update:"Modifier",
        signUp:"S'inscrire",
        signIn:"Se connecter",
        addList:"Ajouter à mes favoris",
        disabledFavorite:"Ce film est dans vos favoris",
        backHome:"Retour à la page d'accueil",
        organize:"Organiser",
        addMovies:"Ajouter des films",
        updateRetro:"Modifier la rétrospective",
        participate:"Participer à la rétrospective",
        deleteRetro:"Supprimer la rétrospective",
        cancelParticipation:"Ne plus participer à la rétrospective"
    }

}
export default messages;