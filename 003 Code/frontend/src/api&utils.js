
export function getTest(){
    return fetch('https://api.themoviedb.org/3/movie/top_rated?api_key=505148347d18c10aeac2faa958dbbf5c')
    .then((res) => res.json());
}

export function makeImagePath(id, format){
    return `https://image.tmdb.org/t/p/${format?format:'original'}/${id}`;
    
}
