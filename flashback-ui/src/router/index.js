import { createRouter, createWebHistory } from 'vue-router';
import guards from './guards';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/admin',
      name: 'admin',
      components: { 
        view : () => import("../components/movies/MoviesToEdit.vue"),
        header : () => import("../components/commons/headers/HeaderAdmin.vue")
      },
      meta:{
        requiresAuth:true,
        role:'ADMIN'
      }
    },{
      path: '/admin/create',
      name: 'movie-create',
      components: {
        view : () => import("../components/movies/CreateMovie.vue"),
        header : () => import("../components/commons/headers/HeaderAdmin.vue")
      },
      meta:{
        requiresAuth:true,
        role:'ADMIN'
      }
    },{
      path: '/admin/:id/update',
      name: 'movie-update',
      components: { 
        view : () => import('../components/movies/UpdateMovie.vue'),
        header : () => import("../components/commons/headers/HeaderAdmin.vue")
      },
      meta:{
        requiresAuth:true,
        role:'ADMIN'
      }
    },{
      path: '/sign-in',
      name: 'sign-in',
      components: {
        view : () => import('../components/beforeAuthentication/SignIn.vue'),
        header : () => import("../components/commons/headers/Header.vue")
      }
    },{
      path: '/',
      name: 'welcome',
      components: {
        view : () => import('../components/beforeAuthentication/Welcome.vue'),
        header : () => import("../components/commons/headers/Header.vue")
      }
    },{
      path: '/sign-up',
      name: 'sign-up',
      components: {
        view : () => import('../components/beforeAuthentication/SignUp.vue'),
        header : () => import("../components/commons/headers/Header.vue")
      }
    },{
      path: '/search',
      name: 'search',
      components: {
        view : () => import('../components/movies/SearchMovie.vue'),
        header : () => import("../components/commons/headers/HeaderUser.vue")
      },
      meta:{
        requiresAuth:true,
        role:'USER'
      }
    },{
      path:'/search/:id/details',
      name:'details',
      components:{
        view: () => import('../components/movies/DetailsMovie.vue'),
        header : () => import("../components/commons/headers/HeaderUser.vue")
      },
      meta:{
        requiresAuth:true,
        role:'USER'
      }
    },{
      path:'/favorites',
      name:'favorites',
      components:{
        view: () => import('../components/movies/Favorite.vue'),
        header : () => import("../components/commons/headers/HeaderUser.vue")
      },
      meta:{
        requiresAuth:true,
        role:'USER'
      }
    },{
      path:'/organize-retrospective',
      name:'organize-retrospective',
      components:{
        view: () => import('../components/retrospectives/OrganizeRetro.vue'),
        header : () => import("../components/commons/headers/HeaderUser.vue")
      },
      meta:{
        requiresAuth:true,
        role:'USER'
      }
    },{
      path:'/planning',
      name:'planning',
      components:{
        view: () => import('../components/retrospectives/Planning.vue'),
        header : () => import("../components/commons/headers/HeaderUser.vue")
      },
      meta:{
        requiresAuth:true,
        role:'USER'
      }
    },{
      path:'/calendar',
      name:'calendar',
      components:{
        view: () => import('../components/retrospectives/Calendar.vue'),
        header : () => import("../components/commons/headers/HeaderUser.vue")
      },
      meta:{
        requiresAuth:true,
        role:'USER'
      }
    },{
      path:'/retrospectives/to-come',
      name:'retrospectives-to-come',
      components:{
        view: () => import('../components/retrospectives/RetrospectivesToCome.vue'),
        header : () => import("../components/commons/headers/HeaderUser.vue")
      },
      meta:{
        requiresAuth:true,
        role:'USER'
      }
    },{
      path:'/retrospectives/:id/details',
      name:'retro-details',
      components:{
        view: () => import('../components/retrospectives/RetroDetails.vue'),
        header : () => import("../components/commons/headers/HeaderUser.vue")
      },
      meta:{
        requiresAuth:true,
        role:'USER'
      }
    },{
      path:'/retrospectives/:id/update',
      name:'retrospective-update',
      components:{
        view: () => import('../components/retrospectives/UpdateRetro.vue'),
        header : () => import("../components/commons/headers/HeaderUser.vue")
      },
      meta:{
        requiresAuth:true,
        role:'USER'
      }
    },{
      path:'/admin/retrospectives',
      name:'retrospectives-admin',
      components:{
        view: () => import('../components/retrospectives/RetroToEdit.vue'),
        header : () => import("../components/commons/headers/HeaderAdmin.vue")
      },
      meta:{
        requiresAuth:true,
        role:'ADMIN'
      }
    },{
      path:'/admin/retrospectives/:id/details',
      name:'details-test',
      components:{
        view: () => import('../components/retrospectives/RetroDetails.vue'),
        header : () => import("../components/commons/headers/HeaderAdmin.vue")
      },
      meta:{
        requiresAuth:true,
        role:'ADMIN'
      }
    },{
      path:'/admin/users',
      name:'admin-users',
      components:{
        view: () => import('../components/users/UsersToEdit.vue'),
        header : () => import("../components/commons/headers/HeaderAdmin.vue")
      },
      meta:{
        requiresAuth:true,
        role:'ADMIN'
      }
    },{
      path:'/not-found',
      name:'not-found',
      components: {
        view: () => import('../components/errors/NotFound.vue')
      }
    },{
      //Vue 3 use regexp to look for a path which does not match any routes
      path:'/:catchAll(.*)',
      redirect:'/not-found'
    },{
      path:'/forbidden',
      name:'forbidden',
      components:{
        view: () => import('../components/errors/Forbidden.vue')
      } 
    },{
      path:'/internal-error',
      name:'internal-error',
      components: {
        view: () => import('../components/errors/InternalError.vue')
      }
    },{
      path:'/unauthorized',
      name:'unauthorized',
      components:{
        view : () => import('../components/errors/Unauthorized.vue')
      },
      meta:{
        requiresAuth:true
      }
    },
  ]
})

router.beforeEach(guards);

export default router;
