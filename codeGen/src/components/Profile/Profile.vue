<script setup></script>

<template>
    <div id="nav">
        <b-navbar-brand to="/">
            <img src="@/assets/Logo.svg" alt="logo" id="logo" />
            <span id="logoTitle">WAVR</span>
        </b-navbar-brand>

        <div class="toggle-container">
            <h6 class="toggle-title">Receive notifications</h6>
            <div class="toggle-switch" :class="{ 'enabled': notificationsEnabled }" @click="toggleNotifications">
                <div class="circle"></div>
            </div>
        </div>

        <b-navbar-nav class="ml-auto">
            <b-nav-item to="/dashboard"><img src="@/assets/Transaction/x.svg" /></b-nav-item>
        </b-navbar-nav>
    </div>
    <div class="profilePage">
        <div class="personalDetailsPanel">
            <span>PERSONAL DETAILS</span>
            <div class="personalDataContainer">
                <h6>First name:<p class="personalData">{{ user ? user.firstName : "" }}</p>
                </h6>
                <h6>Last name: <p class="personalData">{{ user ? user.lastName : "" }}</p>
                </h6>
                <h6>BSN:<p class="personalData"> {{ user ? user.bsn : "" }}</p>
                </h6>
                <h6>Date of birth:<p class="personalData"> {{ user ? user.dateOfBirth : "" }}</p>
                </h6>
                <h6>Email: <input :value="user.email" type="text" class="personalData">
                </h6>
                <!-- <h6>Password: <input v-model="user.password" type="password" class="personalData" ref="passwordInput">
                </h6> -->
            </div>
            <b-button class="button" variant="dark_primary" v-on:click="updateUserDataBtn()">Update my data</b-button>
            <p class="text-danger errorMsg">{{ errorMsg }}</p>
            <span class="tos">You may change your email and password, but for other changes on personal data please call the
                bank.</span>
        </div>
        <div class="accountsPanel">
            <span>MY ACCOUNTS</span>
            <div class="accountsContainer">
                <b-table class="table" :items="accounts" :fields="accountFields"></b-table>
            </div>
            <b-button class="button" variant="dark_primary" v-on:click="requestAccountBtn()">Request new account</b-button>
            <p class="text-danger errorMsg">{{ errorMsg }}</p>
            <span class="tos">These are all your accounts.</span>
        </div>
        <div class="transactionHistoryPanel">
            <span>MY TRANSACTIONS</span>
            <div class="transactionHistoryContainer">
                <b-table class="table" :items="transactions" :fields="transactionFields"></b-table>
            </div>
            <b-button class="button" variant="dark_primary" v-on:click="makeTransactionBtn()">Make new
                transaction</b-button>
            <p class="text-danger errorMsg">{{ errorMsg }}</p>
            <span class="tos">This is your transaction history for the past month.</span>
        </div>
    </div>

    <!-- popup -->
    <div class="card" v-if="responsePopupEnabled">
        <div class="header">
            <!-- Loading -->
            <div class="spinnerContainer" v-if="loading">
                <div class="spinner"></div>
                <div class="loader">
                    <div class="words">
                        <span class="word">Updating your data</span>
                        <span class="word">1 more second</span>
                    </div>
                </div>
            </div>
            <!-- /Loading -->

            <div class="image" v-if="!loading">
                <img src="@/assets/SuccessIcon.png" class="success icon" />
            </div>
            <div class="content" v-if="!loading">
                <span class="title">Your data updated successfully</span>
            </div>
            <div class="actions">
                <b-button variant="dark_primary" class="history" v-on:click="this.$router.push('/dashboard/profile')"
                    v-if="!loading">Done</b-button>
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
                firstName: '',
                lastName: '',
                bsn: '',
                dateOfBirth: '',
                email: '',
                password: ''
            },
            accounts: null,
            transactions: null,
            errorMsg: '',
            accountFields: [
                { key: 'iban', label: 'IBAN' },
                { key: 'typeOfAccount', label: 'Account type' },
                { key: 'balance', label: 'Balance' },
            ],
            transactionFields: [
                { key: 'fromAccount', label: 'From IBAN' },
                { key: 'toAccount', label: 'To IBAN' },
                { key: 'typeOfTransaction', label: 'Transaction type' },
                { key: 'amount', label: 'Amount' },
                { key: 'dateOfExecution', label: 'Date' },
            ],
            responsePopupEnabled: false,
            loading: false,
            delay: (ms) => new Promise((res) => setTimeout(res, ms)),
            notificationsEnabled: false,

        };
    },
    mounted() {
        this.getUser();
        this.getAccountsByUserId();
        this.getTransactionsByUserId();
    },
    methods: {
        // togglePasswordVisibility() {
        //     const passwordInput = this.$refs.passwordInput;
        //     if (passwordInput.type === 'password') {
        //         passwordInput.type = 'text';
        //     } else {
        //         passwordInput.type = 'password';
        //     }
        // },
        async getUser() {
            try {
                let user = await this.$store.dispatch('getUser', this.$store.getters.getUserId);
                this.user = user;
            } catch (error) {
                this.errorMsg = error;
            }
        },
        async getAccountsByUserId() {
            try {
                this.accounts = await this.$store.dispatch('getAccountsByUserId', this.$store.getters.getUserId);
            } catch (error) {
                this.errorMsg = error;
            }
        },
        async getTransactionsByUserId() {
            try {
                // let currentDate = new Date();
                // let monthAgo = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, currentDate.getDate());

                // let filteredTransactions = (await this.$store.dispatch('getTransactionsByUserId', this.$store.getters.getUserId)).filter(transaction => {
                //     let transactionDate = new Date(transaction.dateOfExecution);
                //     return transactionDate >= monthAgo;
                // });
                // this.transactions = await [filteredTransactions];

                this.transactions = await this.$store.dispatch('getTransactionsByUserId', this.$store.getters.getUserId);
            } catch (error) {
                this.errorMsg = error;
            }
        },
        async requestAccountBtn() {
            window.location.href = "/dashboard/requestAccount";
        },
        async makeTransactionBtn() {
            this.$router.push({ name: 'transfer', params: { accounts: "22" } });
        },
        async updateUserDataBtn() {
            try {
                console.log('start updating')
                const updateUserData = {
                    firstName: this.user.firstName,
                    lastName: this.user.lastName,
                    bsn: this.user.bsn,
                    dateOfBirth: this.user.dateOfBirth,
                    email: this.user.email,
                    password: this.user.password,
                    role: this.user.role,
                    dayLimit: this.user.dayLimit,
                    transactionLimit: this.user.transactionLimit

                };

                const currentUser = { ...this.user };
                const updatedUser = {
                    ...currentUser,
                    ...updateUserData
                };;

                console.log('continue updating')

                await this.$store.dispatch('updateUser', {
                    userId: this.$store.state.userId,
                    user: updatedUser
                });

                console.log('finish updating')

                this.responsePopupEnabled = true;
                this.loading = true;
                await this.delay(1500);
                this.loading = false;

            } catch (error) {
                this.errorMsg = error;
            }
        },
        toggleNotifications() {
            this.notificationsEnabled = !this.notificationsEnabled;
        }
    },
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
    background: url('@/assets/dashboard/bg.jpg');
    background-repeat: repeat;
    background-clip: border-box;
    background-position-x: center;
    background-size: 100% 100%;
    height: 100%;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: center;
    padding: 10px 0;

}


.personalDetailsPanel {
    width: 20%;
    margin: 20px auto;
    height: 80%;
    background: var(--white);
    min-height: fit-content;
    border-radius: 35px;
    background-color: var(--white);
    margin-top: 100px;
    margin-left: 20px;
    display: flex;
    flex-direction: column;
    padding: 40px;
    align-items: center;
}

.accountsPanel {
    width: 30%;
    margin: 20px auto;
    height: 80%;
    background: var(--white);
    min-height: fit-content;
    border-radius: 35px;
    background-color: var(--white);
    margin-top: 100px;
    margin-left: 20px;
    display: flex;
    flex-direction: column;
    padding: 40px;
    align-items: center;
}

.transactionHistoryPanel {
    width: 40%;
    margin: 20px auto;
    height: 80%;
    background: var(--white);
    min-height: fit-content;
    border-radius: 35px;
    background-color: var(--white);
    margin-top: 100px;
    margin-left: 20px;
    display: flex;
    flex-direction: column;
    padding: 40px;
    align-items: center;
}

::v-deep .table tbody td {
    font-size: small;
    font-style: inherit;
    color: var(--blue-dark);
}

.personalData {
    font-size: small;
    font-style: inherit;
    margin-top: 5px;
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
    margin-bottom: 80px;
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
</style>
