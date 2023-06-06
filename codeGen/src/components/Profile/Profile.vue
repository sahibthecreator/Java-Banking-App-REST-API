<script setup>
import Navigation from '@/components/Navigation.vue';
</script>

<template>
    <div id="header">
        <navigation />
    </div>
    <div class="profilePage">
        <div class="dataWrapper">

            <h1 class="dataTitle">PERSONAL DETAILS</h1>
            <div class="d-flex userCard">
                <AccountIcon :accountName="user.firstName" class="icon"></AccountIcon>
                <div class="userInfo">
                    <div class="detailCards">
                        <div class="detailCard">
                            <label>First Name</label>
                            <input class="editable" type="text" v-model="user.firstName" @input="editBtnContent = 'Save'">
                        </div>
                        <div class="detailCard">
                            <label>Last Name</label>
                            <input class="editable" type="text" v-model="user.lastName">
                        </div>
                    </div>
                    <div class="detailCard">
                        <label>Email</label>
                        <input class="editable" type="text" v-model="user.email">
                    </div>
                    <div class="detailCard">
                        <label>BSN</label>
                        <span type="text">{{ user.bsn }}</span>
                    </div>
                    <div class="detailCard">
                        <label>Day Limit</label>
                        <span type="text">€ {{ user.dayLimit }} </span>
                    </div>
                    <div class="detailCard">
                        <label>Transaction Limit</label>
                        <span type="text">€ {{ user.transactionLimit }} </span>
                    </div>
                    <div class="detailCard">
                        <label>Accounts</label>
                        <span type="text">{{ accounts }}</span>
                    </div>
                    <div class="detailCard">
                        <label>Total Balance</label>
                        <span type="text">€ {{ totalBalance }}</span>
                    </div>
                </div>

                <div class="userControls">
                    <button class="button btn-warning" @click="updateUser()">{{ editBtnContent }}</button>
                    <button class="button btn-danger" @click="goToLogin()">Logout</button>
                </div>
            </div>
        </div>

    </div>

    <!-- popup -->
    <div class="card" v-if="statusPopupCardEnabled">
        <div class="header">
            <!-- Loading -->
            <div class="spinnerContainer" v-if="loading">
                <div class="spinner"></div>
                <div class="loader">
                    <div class="words">
                        <span class="word">Updating</span>
                        <span class="word">1 more second</span>
                    </div>
                </div>
            </div>
            <!-- /Loading -->

            <div class="image" v-if="!loading">
                <img src="@/assets/SuccessIcon.png" class="success icon" />
            </div>
            <div class="content" v-if="!loading">
                <span class="title">{{ statusTitle }}</span>
                <span v-if="ifEmailChanged()" class="title">{{ statusSubTitle }}</span>
            </div>
            <div class="actions">
                <button type="button" class="history" @click="statusPopupCardEnabled = false"
                    v-if="!loading && !ifEmailChanged()">Close</button>
                <button type="button" class="history" @click="goToLogin()" v-if="!loading && ifEmailChanged()">Login</button>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    name: 'Profile',
    data() {
        return {
            userId: this.$store.state.userId,
            user: {
                firstName: "",
                lastName: "",
                bsn: "",
                dateOfBirth: "",
                email: "",
            },
            accounts: null,
            totalBalance: 0,
            errorMsg: "",
            responsePopupEnabled: false,
            loading: false,
            delay: (ms) => new Promise((res) => setTimeout(res, ms)),
            editBtnContent: "Edit",
            statusPopupCardEnabled: false,
            statusTitle: "",
            statusSubTitle: "",
            initialEmail: "",
        };
    },
    mounted() {
        this.getUser();
        this.getAllAccounts();

    },
    methods: {
        async getUser() {
            try {
                let user = await this.$store.dispatch('getUser', this.$store.getters.getUserId);
                this.user = user;
                this.initialEmail = user.email;
            } catch (error) {
                this.errorMsg = error;
            }
        },
        async updateUser() {
            console.table(this.user);
            let request = {
                userId: this.$store.state.userId,
                userData: this.user
            }
            try {
                await this.$store.dispatch('updateUser', request);
                this.loading = true;
                this.statusPopupCardEnabled = true;
                this.statusTitle = `User updated successfully`;
                if (this.ifEmailChanged()) {
                    this.statusTitle = `User updated successfully`;
                    this.statusSubTitle = `You need to login again because of changing your email`;
                }
                await this.delay(500);
                this.loading = false;
            } catch (error) {
                alert(error.message);
            }

        },
        async updateUserBtn() {
            try {
                console.log('start updating')

                await this.$store.dispatch('updateUser', {
                    userId: this.$store.state.userId,
                    userData: this.user
                });

                console.log('finish updating')

                this.loading = true;
                this.responsePopupEnabled = true;
                await this.delay(1500);
                this.loading = false;

            } catch (error) {
                this.errorMsg = error;
            }
        },
        async getAllAccounts() {
            try {
                let accounts = await this.$store.dispatch(
                    'getAccountsByUserId',
                    this.$store.state.userId,
                );
                this.accounts = accounts.length;

                let value = accounts?.reduce((sum, account) => sum + account.balance, 0)
                let val = (value / 1).toFixed(2).replace('.', ',');
                this.totalBalance = val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
            } catch (error) {
                this.accounts = 0;
            }
        },
        toggleNotifications() {
            this.notificationsEnabled = !this.notificationsEnabled;
        },
        ifEmailChanged() {
            return this.initialEmail.localeCompare(this.user.email);
        },
        goToLogin() {
            this.$store.dispatch('logout');
            this.$router.push('/login')
        },
    },
    // mutations: {
    //     updateUserEmailInComponent(state, userEmail) {
    //         state.user.email = userEmail;
    //     },
    // }
};
</script>

<style lang="scss" scoped>
#nav {
    position: absolute;
    z-index: 10;
    background-color: rgba(255, 255, 255, 0.066);
    width: 100%;
    height: 100px;
    display: flex;
    flex-direction: row;
    align-items: center;
    padding: 0 150px;
}

.profilePage {
    height: fit-content;
    width: 95%;
    background-color: var(--white);
    display: flex;
    align-items: center;
    padding: 10px 5%;
}

.dataTitle {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    margin-top: 80px;
    font-size: 20px;
    font-weight: bold;
    color: var(--blue-dark);
}


.dataPanel {
    width: 50%;
    margin: 20px auto;
    height: 80%;
    background: var(--white);
    min-height: fit-content;
    border-radius: 35px;
    background-color: var(--white);
    display: flex;
    flex-direction: column;
    padding: 20px;
    align-items: center;
}

.data {
    font-size: medium;
    font-style: inherit;
    margin-top: 20px;
    color: var(--blue-dark);
}


.tos {
    text-align: center;
    width: 60%;
    font-size: 12px;
    color: var(--gray-light);
    position: absolute;
    bottom: 0;
    margin-bottom: 5px;
}


.button {
    position: absolute;
    bottom: 0;
    margin-bottom: 50px;
}

.toggle-container {
    position: absolute;
    right: 20%;
    align-items: center;
    margin-left: 20px;
}

.toggle-title {
    font-size: small;
    font-style: inherit;
    margin-right: 10px;
    // font-size: 14px;
    font-weight: bold;
    color: var(--blue-dark);

}

.toggle-switch {
    position: relative;
    width: 40px;
    height: 20px;
    border-radius: 10px;
    background-color: #ccc;
    cursor: pointer;
    transition: background-color 0.3s;
}

.toggle-switch.enabled {
    background-color: #2ecc71;
}

.circle {
    position: absolute;
    top: 2px;
    left: 2px;
    width: 16px;
    height: 16px;
    border-radius: 50%;
    background-color: #fff;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    transition: left 0.3s, background-color 0.3s;
}

.toggle-switch.enabled .circle {
    left: 22px;
    background-color: #fff;
}

.card {
    overflow: hidden;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: left;
    border-radius: 0.5rem;
    max-width: 290px;
    box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1),
        0 10px 10px -5px rgba(0, 0, 0, 0.04);
    background-color: #fff;
}

.header {
    padding: 1.25rem 1rem 1rem 1rem;
}

.image {
    display: flex;
    margin-left: auto;
    margin-right: auto;
    background-color: #e2feee;
    flex-shrink: 0;
    justify-content: center;
    align-items: center;
    width: 4rem;
    height: 4rem;
    border-radius: 9999px;
    animation: animate 0.6s linear alternate-reverse infinite;
    transition: 0.6s ease;

    img {
        height: 80%;
        width: 80%;
    }
}

.content {
    margin-top: 0.75rem;
    text-align: center;
}

.title {
    color: #000000;
    font-size: 1rem;
    font-weight: 500;
    line-height: 1.5rem;
}

.message {
    margin-top: 0.5rem;
    color: #595b5f;
    font-size: 0.875rem;
    line-height: 1.25rem;
}

.actions {
    margin: 0.75rem 1rem;
}

.history {
    display: inline-flex;
    padding: 0.5rem 1rem;
    margin-top: 10%;
    background-color: #263b8f;
    color: #ffffff;
    font-size: 1rem;
    line-height: 1.5rem;
    font-weight: 500;
    justify-content: center;
    width: 100%;
    border-radius: 0.375rem;
    border: none;
    box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);

    &:hover {
        filter: contrast(1.2);
    }
}

@keyframes animate {
    from {
        transform: scale(1);
    }

    to {
        transform: scale(1.09);
    }
}

.errorMsg {
    font-size: smaller;
    margin-bottom: 0;
}

.spinnerContainer {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.spinner {
    width: 56px;
    height: 56px;
    display: grid;
    border: 4px solid #0000;
    border-radius: 50%;
    border-right-color: #299fff;
    animation: tri-spinner 1s infinite linear;
}

.spinner::before,
.spinner::after {
    content: '';
    grid-area: 1/1;
    margin: 2px;
    border: inherit;
    border-radius: 50%;
    animation: tri-spinner 2s infinite;
}

.spinner::after {
    margin: 8px;
    animation-duration: 3s;
}

@keyframes tri-spinner {
    100% {
        transform: rotate(1turn);
    }
}

.loader {
    color: #4a4a4a;
    font-family: 'Poppins', sans-serif;
    font-weight: 500;
    font-size: 23px;
    -webkit-box-sizing: content-box;
    box-sizing: content-box;
    height: 40px;
    padding: 10px 10px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    border-radius: 8px;
}

.words {
    overflow: hidden;
}

.word {
    display: block;
    height: 100%;
    color: #299fff;
    text-align: center;
    animation: cycle-words 3s infinite;
}

@keyframes cycle-words {
    0% {
        -webkit-transform: translateY(50%);
        transform: translateY(50%);
    }

    5% {
        -webkit-transform: translateY(0%);
        transform: translateY(0%);
    }

    30% {
        -webkit-transform: translateY(0%);
        transform: translateY(0%);
    }

    45% {
        -webkit-transform: translateY(-105%);
        transform: translateY(-105%);
    }

    65% {
        -webkit-transform: translateY(-105%);
        transform: translateY(-105%);
    }

    75% {
        -webkit-transform: translateY(-200%);
        transform: translateY(-200%);
    }

    90% {
        -webkit-transform: translateY(-200%);
        transform: translateY(-200%);
    }

    100% {
        -webkit-transform: translateY(-300%);
        transform: translateY(-300%);
    }
}


.userCard {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 5%;


    .userInfo {
        .detailCards {
            display: flex;
            justify-content: space-between;
            width: 100%;

            .detailCard {
                width: 48%;
            }
        }

        .detailCard {
            display: flex;
            flex-direction: column;
            width: 100%;


            input,
            span {
                border: none;
                color: var(--gray-light);
                background: rgb(238, 238, 238);
                box-shadow: 0 0 30px #1414140a;
                border-radius: 7px;
                border: 1px solid rgba(0, 0, 0, 0.125);
                padding: 4px;

                &:focus {
                    outline: none;
                }

                &.editable {
                    background: var(--white);
                    color: rgb(8, 8, 8);
                }
            }

            span {
                cursor: not-allowed;
            }

            label {
                font-size: small;
                margin-bottom: 1%;
                margin-top: 2%;
                color: var(--gray-light);
            }
        }

        .infoList {
            width: 100%;
            flex-direction: column;
            color: var(--gray-light);
            display: flex;
            gap: 5px;

            // span {
            //   margin-block: 10px;
            //   padding-inline: 10px;
            //   border-radius: 35px;
            //   background: var(--gray-black);
            // }
        }
    }

    .userControls {
        margin: 10% 0 5% 0;
        width: fit-content;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        width: 85%;

        button {
            height: fit-content;
            padding-block: 5px;
            width: 47%;
            border-radius: 7px;
            background-color: var(--gray-black);
            border: none;
            color: white;
            cursor: pointer;
            box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.103);
            position: relative;
            overflow: hidden;
            transition-duration: 0.3s;

            &:active {
                transform: translate(5px, 5px);
                transition-duration: 0.3s;
            }
        }
    }
}
</style>
