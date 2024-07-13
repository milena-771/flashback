import axios, { AxiosError } from 'axios';
import router from '@/router/index.js'
import useStore from "@/stores/store.js";

export default {
    install: (app) => {

        const store = useStore();

    // Creation and configuration of an axios instance
    const axiosInstance = axios.create({
        baseURL: import.meta.env.VITE_API_BASE_URL,
    });

    axiosInstance.interceptors.request.use(
        (config) => {
            const token = localStorage.getItem('token');
            const bearerToken = token ? `Bearer ${token}` : null;
            config.headers['Authorization'] = bearerToken;         
            return config;
        },(error) => { 
            return Promise.reject(error);
        })

    axiosInstance.interceptors.response.use(
        (response) => {
            if(response.data.token){
                const accessToken = response.data.token;
                const userName = response.data.firstname;
                const role = response.data.role;
                localStorage.clear();
                localStorage.setItem('token', accessToken);
                localStorage.setItem('isAuthenticated', true);
                localStorage.setItem('role', role);
                localStorage.setItem('userName', userName);

                if(response.data.exp){
                    const tokenExp = response.data.exp;//TODO check today's date
                
                    let timeExp = new Date(tokenExp).getTime();
                    const now = new Date(Date.now()).getTime();
                    const alert = new Date(timeExp - now).getMinutes();
                    const delay = alert * 60000;                  
    
                    if(delay > 0 && localStorage.getItem("isAuthenticated")) {
                        const timer = setTimeout( async ()=> {
                            console.log("Refreshing...");
                            const res = await refreshToken();
                        }, delay)
                        //pass timer to the store to stop settimeout when logout
                        store.timerID = timer;
                        //TODO throw forbidden exception after refresh token has expired
                    }
                }
            }        
        return response;
    },async (error) => {
        console.error(error);
        const status = error.response.status;
        let path=''
        switch(status){
            case 401:
                localStorage.clear();
                path='/unauthorized';
                break;
            case 404:
                localStorage.clear();
                path='/not-found';
                break;
            case 403:
                localStorage.clear(); 
                path='/forbidden';
                break;
            case 500:
                localStorage.clear();
                path='/internal-error';
                break;
        }
        router.push(path)
        return Promise.reject(error);
    });

    function refreshToken() {
        let url = import.meta.env.VITE_API_BASE_URL;
        return axiosInstance.get(url+'/refresh-token');
    }

    // Use of global properties to access axios with its config in all app
   // with $axios
   app.config.globalProperties.$axios = axiosInstance;
   
    }
  }