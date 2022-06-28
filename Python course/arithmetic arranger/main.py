# This entrypoint file to be used in development. Start by reading README.md
#from pytest import main

from arithmetic_arranger import arithmetic_arranger


print("\nGive mathematical equations: ")
eq = []
inp = input()
while inp != "" :
    eq.append(inp)
    inp = input()

print("Do you want to show answers: (Type 'Yes' or 'No')")
answer = input()
while (answer != "Yes" and answer != "No") :
    print("Please refine your answer:")
    answer = input()

if answer == "Yes":
    answer = True
else :
    answer = False

print("\nAnswers:")

print(arithmetic_arranger(eq, answer))

# Run unit tests automatically
#main()
