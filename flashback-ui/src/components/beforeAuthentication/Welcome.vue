<script>
</script>
<template>
    <main class="mx-auto text-center">
        <div class="container-xl">
            <h3 class="pt-3">Bienvenue sur <br>
            <span class="flash-back">
                <span class="flash">{{ $t(`menu.flash`) }}</span><span class="back">{{ $t(`menu.back`) }}</span>
            </span>
        </h3>
        <div class="directorQuote mt-4 mb-4 px-5">
            <span class="quote">"You never stop learning about cinema." </span>
            <span class="fw-bold">Francis Ford Coppola</span>
        </div>
        <div class="screen mx-auto p-0 container-fluid rounded"> 
            <img src="/welcome-illustrations/musical.jpg" class="img-fluid rounded filmRoll mt-4">
            <img src="/welcome-illustrations/night-hunter.jpg" class="img-fluid rounded mx-2 filmRoll mt-4">
            <img src="/welcome-illustrations/mood.png" class="img-fluid rounded filmRoll mt-4">
            <img src="/welcome-illustrations/vampire.jpg" class="img-fluid rounded mx-2 filmRoll mt-4">    
        </div>
        <h4 class="py-5 px-4">Organisez vos rétrospectives pour <span class="retro">(re)découvrir</span> les classiques du cinéma</h4>
        <div class="row row-cols-1 row-cols-md-4 p-0 mx-auto">
            <div class="card h-100 my-4 p-0">
                <div class="card-body text-center p-0">
                    <img src="/logos/white-bookmark-star.png" alt="favorite logo" class="star my-2">
                    <div class="card-text">
                        Choisissez les films que vous souhaitez voir.
                    </div>
                </div>
            </div>
            <div class="card h-100 my-4 p-0">
                <div class="card-body text-center p-0">
                    <img src="/logos/white-calendar.png" alt="calendar logo" class="calendar my-2">
                    <div class="card-text">
                        Planifiez vos rétrospectives.
                    </div>
                </div>
            </div>
            <div class="card h-100 my-4 p-0">
                <div class="card-body text-center p-0">
                    <img src="/logos/white-ticket.png" alt="ticket logo" class="ticket my-2">
                    <div class="card-text">
                       Participez à d'autres rétrospectives.
                    </div>
                </div>
            </div>
            <div class="card h-100 p-0 my-4">
                <div class="card-body text-center p-0">
                    <div class="d-flex justify-content-center align-items-end my-1">
                        <img src="/logos/white-projector.png" alt="projector logo" class="projector mx-2">
                        <img src="/logos/popcorn.png" alt="popcorn logo" class="popcorn mx-2">
                    </div>                       
                    <div class="card-text my-3">
                        Prenez le temps de regarder les films.
                    </div>
                </div>
            </div>
        </div>
        </div>
       
    </main>
</template>
<style scoped>
.flash-back{
    font-size: 1rem;
}
.flash{
    font-size: 3rem;
}
.back{
    font-family: "Libre Baskerville", serif;
    color:blueviolet;
    font-weight: 600;
    font-size: 2.5rem;
    font-style: italic;
}
main {
    border:2px solid black;
    background-color: black;
    min-height: 95vh;
}
h3, h4{
    color:white;
}

.directorQuote{
    color:white;
    font-size: 1.4rem;
}
.quote{
    font-style: italic;   
}

.retro{
    color:blueviolet;
    font-style: italic;
}

.card{
    border: none;
    background-color: black;
}
.card-text{
    color:white;
}

.filmRoll{
    width:20%;
}

.star{
    width:5rem;
}

.calendar{
    width:4.5rem;
}

.ticket{
    width:6.5rem;
}

.projector{
    width:6rem;
}

.popcorn{
    width:2.5rem;
}

</style>